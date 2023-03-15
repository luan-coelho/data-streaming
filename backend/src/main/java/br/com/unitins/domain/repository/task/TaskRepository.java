package br.com.unitins.domain.repository.task;

import br.com.unitins.queue.Task;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskRepository implements PanacheRepository<Task> {

    @Inject
    EntityManager entityManager;

    public List<Task> listAllActive() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.like(root.get("status"), Task.TaskStatus.PROCESSING.toString()));

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<Task> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
