package br.com.unitins.service.video;

import br.com.unitins.domain.model.video.Video;
import br.com.unitins.domain.model.video.VideoMetrics;
import br.com.unitins.domain.repository.video.VideoMetricsRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class VideoMetricsService {

    @Inject
    VideoService videoService;

    @Inject
    VideoMetricsRepository videoMetricsRepository;

    @Transactional
    public void incrementViewsByDate(Long videoId) {
        Video video = videoService.getById(videoId);
        LocalDate today = LocalDate.now();

        if (!videoMetricsRepository.existsByVideoAndDate(videoId, today)) {
            VideoMetrics vm = new VideoMetrics(video, today);
            videoMetricsRepository.getEntityManager().merge(vm);
        }
        videoMetricsRepository.incrementViewsByDate(videoId, today);
        videoService.incrementViews(videoId);
    }

    public List<VideoMetrics> listTopVideosByDate(LocalDate date) {
        return videoMetricsRepository.listTopVideosByDate(date);
    }
}
