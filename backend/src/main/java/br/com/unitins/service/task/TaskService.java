package br.com.unitins.service.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.domain.model.task.Task;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    EntityManager entityManager;

    public List<Task> getAll() {
        String jpql = "SELECT t FROM Task t";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        return query.getResultList();
    }

    public List<Task> getActive() {
        String jpql = "SELECT t FROM Task t WHERE t.status = :status";
        TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);
        query.setParameter("status", TaskStatus.PROCESSING);
        return query.getResultList();
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

    @Transactional
    public void changeStatus(Task task, TaskStatus taskStatus, String details) {
        task.setStatus(taskStatus);
        task.setDetails(details);
        entityManager.merge(task);
    }
}
