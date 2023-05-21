package br.com.unitins.service.video;

import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.model.video.Video;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@TestTransaction
@QuarkusTest
class VideoServiceTest {

    @Inject
    VideoService videoService;

    @Test
    void testGetAll() {
        // Given - When
        Pagination<Video> videos = videoService.getAll(new Pageable(), new VideoFilter());

        // Then
        assertNotNull(videos.getContent());
        assertFalse(videos.getContent().isEmpty());
    }

    @Test
    void testCreate() {
        // Given - When
        Video persistedVideo = getPersistedVideo();

        // Then
        assertNotNull(persistedVideo);
        assertNotNull(persistedVideo.getId());
        assertEquals(persistedVideo.getTitle(), "Java: Aula 1");
        assertEquals(persistedVideo.getDescription(), "Introdução a linguagem");
    }

    @Test
    void testUpdate() {
        // Given
        Video persistedVideo = getPersistedVideo();
        persistedVideo.setTitle("Java: Aula 2");
        persistedVideo.setDescription("Aprendendo testes");

        // When
        videoService.update(persistedVideo.getId(), persistedVideo);
        Video updatedVideo = videoService.getById(persistedVideo.getId());

        // Then
        assertNotNull(updatedVideo);
        assertNotNull(updatedVideo.getId());
        assertEquals(updatedVideo.getTitle(), "Java: Aula 2");
        assertEquals(updatedVideo.getDescription(), "Aprendendo testes");
    }

    @Test
    void testGetById() {
        // Given
        Video persistedVideo = getPersistedVideo();

        // When
        Video video = videoService.getById(persistedVideo.getId());

        // Then
        assertNotNull(video);
        assertNotNull(video.getId());
        assertEquals(video.getTitle(), "Java: Aula 1");
        assertEquals(video.getDescription(), "Introdução a linguagem");
    }

    @Test
    void testDelete() {
        // Given
        Video persistedVideo = getPersistedVideo();

        // When
        videoService.deleteById(persistedVideo.getId());

        // Then
        assertThrows(NotFoundException.class, () -> {
            videoService.getById(persistedVideo.getId());
        });
    }

    private Video getPersistedVideo() {
        Video videoDto = new Video("Java: Aula 1", "Introdução a linguagem");
        Video persistedVideo = videoService.create(videoDto);
        return videoService.getById(persistedVideo.getId());
    }
}