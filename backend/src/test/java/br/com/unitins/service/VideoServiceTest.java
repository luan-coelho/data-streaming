package br.com.unitins.service;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.Video;
import br.com.unitins.domain.VideoRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.*;

@QuarkusTest
class VideoServiceTest {

    @Inject
    VideoService videoService;

    @Inject
    VideoRepository videoRepository;

    @Transactional
    @Test
    public void testSaveResourceFile() throws IOException {
        Video video = new Video("Iniciando no Quarkus", "Começando no Framework");
        videoRepository.persist(video);

        File file = new File("video_noticia.mp4");
        MultipartBody body = new MultipartBody();
        body.file = file;
        body.fileName = "video_noticia.mp4";
        body.inputStream = new FileInputStream(file);

        videoService.saveResourceFile(video.getId(), body);
    }
}