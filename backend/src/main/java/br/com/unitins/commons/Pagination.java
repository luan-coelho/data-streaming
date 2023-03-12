package br.com.unitins.commons;

import br.com.unitins.commons.pagination.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {

    private List<T> content;
    private int numberOfElements;
    private int page;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private Sort sort = new Sort();

    public static final int DEFAULT_PAGINATION_SIZE = 10;

    public static <T> Pagination<T> of(List<T> content, int page, int size, long totalElements) {
        Pagination<T> paginationInstance = new Pagination<>();
        paginationInstance.setContent(content);
        paginationInstance.setNumberOfElements(content.size());
        paginationInstance.setPage(page);
        paginationInstance.setPageSize(size);
        paginationInstance.setTotalPages((int) (totalElements / size));
        paginationInstance.setTotalElements(totalElements);

        return paginationInstance;
    }

    public static <T> Pagination<T> of(List<T> content, Pageable pageable, long totalElements) {
        Pagination<T> paginationInstance = new Pagination<>();
        paginationInstance.setContent(content);
        paginationInstance.setNumberOfElements(content.size());
        paginationInstance.setPage(pageable.getPage());
        paginationInstance.setPageSize(pageable.getSize());
        paginationInstance.setTotalPages((int) (totalElements / pageable.getSize()));
        paginationInstance.setTotalElements(totalElements);
        paginationInstance.getSort().setField(pageable.getSort());
        paginationInstance.getSort().setOrder(pageable.getOrder().toString());

        return paginationInstance;
    }
}
