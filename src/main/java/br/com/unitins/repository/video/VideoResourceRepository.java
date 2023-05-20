package br.com.unitins.repository.video;

import br.com.unitins.model.video.VideoResource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VideoResourceRepository implements PanacheRepository<VideoResource> {
}
