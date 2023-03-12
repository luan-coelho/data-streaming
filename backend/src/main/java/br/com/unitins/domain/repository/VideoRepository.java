package br.com.unitins.domain.repository;

import br.com.unitins.commons.Pageable;
import br.com.unitins.commons.Pagination;
import br.com.unitins.domain.model.Video;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VideoRepository implements PanacheRepository<Video> {

    public Pagination<Video> listAllPaginated(Pageable pageable) {
        PanacheQuery<Video> list = findAll(Sort.by(pageable.getSort(), pageable.getOrder()));
        List<Video> monthlyBudgets = list.page(Page.of(pageable.getPage(), pageable.getSize())).list();

        return Pagination.of(monthlyBudgets, pageable, count());
    }

    public boolean existsByTitle(String title) {
        return find("title", title).count() > 0;
    }

    public Optional<Video> findByTitle(String title) {
        return find("title", title).singleResultOptional();
    }
}
