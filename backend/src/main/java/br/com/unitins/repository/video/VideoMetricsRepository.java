package br.com.unitins.repository.video;

import br.com.unitins.domain.model.video.VideoMetrics;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class VideoMetricsRepository implements PanacheRepository<VideoMetrics> {

    public boolean existsByVideoAndDate(Long videoId, LocalDate date) {
        Long count = getEntityManager().createQuery("SELECT COUNT (v) FROM VideoMetrics v WHERE v.video.id = :videoId AND v.date = :date", Long.class)
                .setParameter("videoId", videoId)
                .setParameter("date", date)
                .getSingleResult();
        return count > 0;
    }

    public void incrementViewsByDate(Long videoId, LocalDate date) {
        getEntityManager().createQuery("UPDATE VideoMetrics v SET v.views = v.views + 1 WHERE v.video.id = :videoId AND v.date = :date")
                .setParameter("videoId", videoId)
                .setParameter("date", date)
                .executeUpdate();
    }

    public List<VideoMetrics> listTopVideosByDate(LocalDate date) {
        return getEntityManager().createQuery("FROM VideoMetrics v WHERE v.date = :date ORDER BY v.views DESC", VideoMetrics.class)
                .setParameter("date", date).setMaxResults(5).getResultList();
    }
}
