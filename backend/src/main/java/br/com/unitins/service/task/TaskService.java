package br.com.unitins.service.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.repository.task.TaskRepository;
import br.com.unitins.queue.Task;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Transactional
    public Task create(Task task) {
        taskRepository.persist(task);
        return task;
    }

    @Transactional
    public Task changeStatus(Task task, TaskStatus taskStatus) {
        task.setStatus(taskStatus);
        taskRepository.persist(task);
        return task;
    }

    public List<Task> getAll() {
        return taskRepository.listAll();
    }

    public List<Task> getActive() {
        return taskRepository.listAllActive();
    }
}
