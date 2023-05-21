package br.com.unitins.service.video;

import br.com.unitins.model.video.Video;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class VideoServiceTest {

    @Inject
    VideoService videoService;

    @Test
    void testGetAll() {

    }

    @Test
    void testCreate() {
        Video video = getPersistedVideo();

        assertNotNull(video);
        assertNotNull(video.getId());
        assertEquals(video.getTitle(), "Java: Aula 1");
        assertEquals(video.getDescription(), "Introdução a linguagem");
    }

    @Test
    void testUpdate() {
        Video persistedVideo = getPersistedVideo();
        persistedVideo.setTitle("Java: Aula 2");
        persistedVideo.setDescription("Aprendendo testes");

        videoService.update(persistedVideo.getId(), persistedVideo);

        Video video = videoService.getById(persistedVideo.getId());

        assertNotNull(video);
        assertNotNull(video.getId());
        assertEquals(video.getTitle(), "Java: Aula 1");
        assertEquals(video.getDescription(), "Introdução a linguagem");
    }

    @Test
    void testGetById() {
    }

    @Test
    void testDelete() {
    }

    private Video getPersistedVideo() {
        Video videoDto = new Video("Java: Aula 1", "Introdução a linguagem");
        Video persistedVideo = videoService.create(videoDto);
        return videoService.getById(persistedVideo.getId());
    }
}