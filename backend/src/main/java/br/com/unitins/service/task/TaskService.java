package br.com.unitins.service.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.task.Task;
import br.com.unitins.domain.repository.task.TaskRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    EntityManager entityManager;

    public List<Task> getAll() {
        return taskRepository.listAll();
    }

    public List<Task> getActive() {
        return taskRepository.listAllActive();
    }

    @Transactional
    public Task create(Long videoId) {
        Task task = new Task(videoId);
        return entityManager.merge(task);
    }

    @Transactional
    public void changeStatus(Task task, TaskStatus taskStatus) {
        task.setStatus(taskStatus);
        entityManager.merge(task);
    }
}
