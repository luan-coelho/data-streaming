package br.com.unitins.service;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.model.video.Video;
import br.com.unitins.domain.repository.video.VideoRepository;
import br.com.unitins.queue.VideoProcessing;
import br.com.unitins.service.video.VideoService;
import io.quarkus.test.junit.QuarkusTest;
import org.gradle.internal.SystemProperties;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;

@QuarkusTest
class VideoServiceTest {

    private final String BAR = File.separator; // "\" ou "/"

    @Inject
    VideoService videoService;

    @Inject
    VideoProcessing videoProcessing;

    @Inject
    VideoRepository videoRepository;

    @Test
    public void testGetVideoDirectory() {
        String path = "C:\\Users\\lumyt\\midia\\luancoelho\\video.mp4";

        String directory = videoService.getVideoDirectory(path);

        assert directory.equals("C:\\Users\\lumyt\\midia\\luancoelho\\");
    }

    @Transactional
    @Test
    public void testSaveResourceFile() throws Exception {
        Video video = new Video("Iniciando no Quarkus", "Começando no Framework");
        videoRepository.persist(video);

        File file = new File("video.mp4");
        MultipartBody body = new MultipartBody();
        body.file = file;
        body.fileName = "video.mp4";
        body.inputStream = new FileInputStream(file);

//        videoService.saveResourceFile(processProperties);
        videoProcessing.startProcess(video.getId(), body);

        String outputPath = SystemProperties.getInstance().getUserHome() + BAR + "midia" + BAR + "luancoelho" + BAR + 1 + BAR + "video_720.mp4";
        File savedFile = new File(outputPath);

        assert savedFile.exists();
    }
}