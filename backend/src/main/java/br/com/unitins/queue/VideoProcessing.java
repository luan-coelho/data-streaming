package br.com.unitins.queue;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.task.Task;
import br.com.unitins.model.video.Video;
import br.com.unitins.service.task.TaskService;
import br.com.unitins.service.video.VideoService;
import io.quarkus.runtime.ShutdownEvent;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
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

    @Inject
    @ConfigProperty(name = "app.executor.max-threads")
    int nMaxThreads;

    private ExecutorService executor;

    @PostConstruct
    void init() {
        executor = Executors.newFixedThreadPool(nMaxThreads);
    }

    public void startProcess(Long videoId, MultipartBody multipartBody) {
        Video video = videoService.getById(videoId);

        Task task = taskService.create(videoId);
        log.info("Starting asynchronous task of process video resource for id video: {}", videoId);

        CompletableFuture.runAsync(() -> {
            try {
                videoService.saveResourceFile(video, multipartBody);
                taskService.changeStatus(task, TaskStatus.COMPLETED);
                log.info("Asynchronous processing completed successfully for id's video: {}", videoId);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                taskService.changeStatus(task, TaskStatus.INTERRUPTED, e.getMessage());
                log.error("Error when trying to save id video resource: {} - Message: {}", videoId, e.getMessage());
            }
        }, executor);
    }

    public void onStop(@Observes ShutdownEvent ev) {
        executor.shutdown();
    }
}
