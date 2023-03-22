package br.com.unitins.domain.repository.video;

import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.domain.model.video.Video;
import br.com.unitins.rest.filters.VideoFilter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public Pagination<Video> listAllPaginatedByTitle(Pageable pageable, VideoFilter filter) {
        String query = "FROM Video v WHERE 1 = 1";
        Map<String, Object> queryParams = new HashMap<>();

        if (filter.getTitle() != null) {
            query += " AND lower(v.title) LIKE lower(:title)";
            queryParams.put("title", "%" + filter.getTitle() + "%");
        }
        if (filter.getDescription() != null) {
            query += " AND lower(v.description) LIKE lower(:description)";
            queryParams.put("description", "%" + filter.getDescription() + "%");
        }

        String sortOrder = pageable.getOrder().equalsIgnoreCase("ASC") ? "ASC" : "DESC";
        query += " ORDER BY v." + pageable.getSort(Video.class) + " " + sortOrder;

        PanacheQuery<Video> panacheQuery = find(query, queryParams);
        panacheQuery.page(pageable.getPage(), pageable.getSize());

        return Pagination.of(panacheQuery.list(), pageable, count());
    }

    public void incrementViews(Long videoId) {
        getEntityManager().createQuery("UPDATE Video v SET v.views = v.views + 1 WHERE v.id = :videoId")
                .setParameter("videoId", videoId)
                .executeUpdate();
    }

    public boolean existsByTitle(String title) {
        return find("title", title).count() > 0;
    }
}
