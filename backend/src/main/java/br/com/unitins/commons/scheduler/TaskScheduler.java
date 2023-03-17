package br.com.unitins.commons.scheduler;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.task.Task;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@ApplicationScoped
public class TaskScheduler {

    private static final Queue<Task> taskQueue = new ConcurrentLinkedQueue<>();

    @Inject
    EntityManager em;

    @Scheduled(every = "15s")
    @Transactional
    public void processTasks() {
        log.info("TaskScheduler starting...");
        if (taskQueue.isEmpty()) {
            log.info("Nothing to be saved");
            return;
        }

        log.info("Starting saving video processing tasks");
        Iterator<Task> iterator = taskQueue.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();
            em.merge(task);
            if (!task.getStatus().equals(TaskStatus.PROCESSING)) {
                iterator.remove();
            }
        }
        log.info("Saving video processing tasks completed successfully");
    }

    public static void addTask(Task task) {
        taskQueue.add(task);
    }
}
