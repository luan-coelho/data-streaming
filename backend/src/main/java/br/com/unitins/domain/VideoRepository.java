package br.com.unitins.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public boolean findByTitle(String title){
        return find("title", title).count() > 0;
    }
}
