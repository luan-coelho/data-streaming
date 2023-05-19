package br.com.unitins.repository.video;

import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.model.video.Video;
import br.com.unitins.model.video.VideoWatchTime;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

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

    public void persistWatchTime(VideoWatchTime videoWatchTime) {
        getEntityManager().merge(videoWatchTime);
    }

    public void updateWatchTime(VideoWatchTime videoWatchTime) {
        getEntityManager().createQuery("UPDATE VideoWatchTime vw SET vw.watchTime = :watchTime WHERE vw.videoId = :videoId")
                .setParameter("videoId", videoWatchTime.getVideoId())
                .setParameter("watchTime", videoWatchTime.getWatchTime())
                .executeUpdate();
    }

    public boolean existsWatchTimeByVideoId(Long videoId) {
        Query query = getEntityManager().createNativeQuery("SELECT COUNT(vw) FROM VideoWatchTime vw WHERE vw.videoid = :videoId")
                .setParameter("videoId", videoId);
        Object singleResult = query.getSingleResult();
        return (long) singleResult > 0;
    }

    public double getWatchTime(Long videoId) {
        try {
            return getEntityManager().createQuery("FROM VideoWatchTime vw WHERE vw.videoId = :videoId", VideoWatchTime.class)
                    .setParameter("videoId", videoId)
                    .getSingleResult().getWatchTime();
        } catch (NoResultException e) {
            return 0.0;
        }
    }

    public Video findByTitle(String title) {
        return find("title", title).firstResult();
    }

    public boolean existsByTitle(String title) {
        String query = "LOWER(title) = LOWER(:title)";
        Parameters params = Parameters.with("title", title);
        return count(query, params) > 0;
    }
}
