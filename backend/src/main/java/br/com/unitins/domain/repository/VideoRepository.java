package br.com.unitins.domain.repository;

import br.com.unitins.commons.Pageable;
import br.com.unitins.commons.Pagination;
import br.com.unitins.domain.model.Video;
import br.com.unitins.rest.filters.VideoFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

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
public class VideoRepository implements PanacheRepository<Video> {

    @Inject
    EntityManager entityManager;

    public Pagination<Video> listAllPaginated(Pageable pageable) {
        PanacheQuery<Video> list = findAll(Sort.by(pageable.getSort(), pageable.getOrder()));
        List<Video> videoList = list.page(Page.of(pageable.getPage(), pageable.getSize())).list();

        return Pagination.of(videoList, pageable, count());
    }

    public boolean existsByTitle(String title) {
        return find("title", title).count() > 0;
    }

    /*public Pagination<Video> listAllPaginatedByTitle(Pageable pageable, String title) {
        String query = "title LIKE CONCAT('%', :title, '%') ORDER BY " + pageable.getSort();
        Sort sort = Sort.by(pageable.getSort(), pageable.getDirection());
        Parameters parameters = Parameters.with("title", title);

        PanacheQuery<Video> list = find(query, sort, parameters);
        List<Video> videoList = list.page(Page.of(pageable.getPage(), pageable.getSize())).list();

        return Pagination.of(videoList, pageable, count());
    }*/

    public Pagination<Video> listAllPaginatedByTitle(Pageable pageable, VideoFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Video> query = cb.createQuery(Video.class);
        Root<Video> root = query.from(Video.class);
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getTitle() != null) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
        }
        if (filter.getDescription() != null) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        if (pageable.getSort().equals("ASC")) {
            query.orderBy(cb.asc(root.get(pageable.getSort())));
        } else {
            query.orderBy(cb.desc(root.get(pageable.getSort())));
        }

        TypedQuery<Video> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPage() * pageable.getSize());
        typedQuery.setMaxResults(pageable.getSize());

        List<Video> resultList = typedQuery.getResultList();
        return Pagination.of(resultList, pageable, count());
    }
}
