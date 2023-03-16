package br.com.unitins.queue;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.Video;
import br.com.unitins.service.task.TaskService;
import br.com.unitins.service.video.VideoService;
import io.quarkus.runtime.ShutdownEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.ActivateRequestContext;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ApplicationScoped
public class VideoProcessing {

    @Inject
    VideoService videoService;

    @Inject
    TaskService taskService;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);


    @Transactional
    public void startProcess(Long videoId, MultipartBody multipartBody) {
        Video video = videoService.getById(videoId);

        Task task = taskService.create(videoId);// TaskStatus.PROCESSING
        log.info("Starting asynchronous task of process video resource for id video: {}", videoId);

        run(videoId, multipartBody, video, task);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void run(Long videoId, MultipartBody multipartBody, Video video, Task task) {
        CompletableFuture.runAsync(() -> {
            try {
                videoService.saveResourceFile(video, multipartBody);

                taskService.changeStatus(task, TaskStatus.COMPLETED);
                log.warn("Asynchronous processing completed successfully for id's video: {}", videoId);
            } catch (Exception e) {
                Thread.currentThread().interrupt();

                taskService.changeStatus(task, TaskStatus.INTERRUPTED, e.getMessage());
                log.error("Error when trying to save id video resource: {} - Message: {}", videoId, e.getMessage());
            }
        }, executor);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void onStop(@Observes ShutdownEvent ev) {
        shutdown();
    }
}
