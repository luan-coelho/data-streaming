package br.com.unitins.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public boolean existsByTitle(String title) {
        return find("title", title).count() > 0;
    }

    public Optional<Video> findByTitle(String title) {
        return find("title", title).singleResultOptional();
    }
}
