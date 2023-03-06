package br.com.unitins;

import br.com.unitins.service.cache.VideoCache;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;

@QuarkusTest
public class VideoCacheTest {

    @Inject
    VideoCache videoCache;

    @Test
    public void testPut(){
        File file = new File("video.mp4");

        videoCache.put("Video1", file);
        videoCache.put("Video2", file);
        videoCache.put("Video3", file);
        videoCache.put("Video4", file);
        videoCache.put("Video5", file);
    }
}
