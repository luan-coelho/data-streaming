package br.com.unitins.service.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.task.Task;
import br.com.unitins.domain.repository.task.TaskRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    public Task create(Long videoId) {
        Task task = new Task();
        task.setVideoId(videoId);
        task.setStatus(TaskStatus.PROCESSING);
        taskRepository.persist(task);
        return task;
    }

    public void changeStatus(Task task, TaskStatus taskStatus) {
        task.setStatus(taskStatus);
        taskRepository.persist(task);
    }

    public void changeStatus(Task task, TaskStatus taskStatus, String description) {
        task.setStatus(taskStatus);
        task.setDescription(description);
        taskRepository.persist(task);
    }

    public List<Task> getAll() {
        return taskRepository.listAll();
    }

    public List<Task> getActive() {
        return taskRepository.listAllActive();
    }
}
