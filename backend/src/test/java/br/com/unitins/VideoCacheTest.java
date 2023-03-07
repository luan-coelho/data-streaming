package br.com.unitins;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.Video;
import br.com.unitins.service.cache.VideoCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@QuarkusTest
public class VideoCacheTest {

    @Inject
    VideoCache videoCache;

    @Test
    public void testGetAndPut(){
        File file = new File("video.mp4");

        Video video1 = new Video("Quarkus");
        Video video2 = new Video("Spring Framework");

        videoCache.put("Video1", video1);
        videoCache.put("Video2", video2);

        Assertions.assertTrue(file.exists());
        Assertions.assertTrue(file.exists());
        Assertions.assertEquals(videoCache.getNumberOfVideos(), 2);
    }
}
