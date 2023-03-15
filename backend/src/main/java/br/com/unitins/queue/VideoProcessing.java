package br.com.unitins.queue;

import br.com.unitins.commons.ProcessProperties;
import br.com.unitins.service.video.VideoService;
import io.quarkus.runtime.ShutdownEvent;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@ApplicationScoped
public class VideoProcessing {

    @Inject
    VideoService videoService;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    public CompletableFuture<Void> executeAsyncTask(ProcessProperties processProperties) {
        Task task = Task.create(processProperties.getVideoId());

        return CompletableFuture.runAsync(() -> {
            try {
                videoService.saveResourceFile(processProperties);
                task.changeStatusToCompleted();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                task.changeStatusToInterrupted();
            }
        }, executor);
    }

    public Task getTaskStatus(Long id) {
        return tasks.get(id);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void onStop(@Observes ShutdownEvent ev) {
        shutdown();
    }
}
