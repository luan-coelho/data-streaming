package br.com.unitins.service;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.model.Video;
import br.com.unitins.domain.repository.VideoRepository;
import br.com.unitins.service.video.VideoService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@QuarkusTest
class VideoServiceTest {

    private final String BAR = File.separator; // "\" ou "/"

    @Inject
    VideoService videoService;

    @Inject
    VideoRepository videoRepository;

    @Transactional
    @Test
    public void testSaveResourceFile() throws IOException {
        Video video = new Video("Iniciando no Quarkus", "Começando no Framework");
        videoRepository.persist(video);

        File file = new File("video.mp4");
        MultipartBody body = new MultipartBody();
        body.file = file;
        body.fileName = "video.mp4";
        body.inputStream = new FileInputStream(file);

        videoService.saveResourceFile(video.getId(), body);

        String outputPath = BAR + "midia" + BAR + "luancoelho" + BAR + 1 + BAR + "video_720.mp4";
        File savedFile = new File(outputPath);

        assert savedFile.exists();
    }
}