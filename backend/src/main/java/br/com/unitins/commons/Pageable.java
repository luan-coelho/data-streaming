package br.com.unitins.commons;

import io.quarkus.panache.common.Sort;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;

@Setter
@Getter
public class Pageable {

    @QueryParam("page")
    private int page = 0;
    @QueryParam("size")
    private int size = Pagination.DEFAULT_PAGINATION_SIZE;
    @QueryParam("sort")
    private String sort;

    public String getSort() {
        if (this.sort != null) {
            if (this.sort.contains(",")) {
                String[] properties = this.sort.split(",");
                return properties[0];
            }
        }
        return "id";
    }

    public Sort.Direction getOrder() {
        if (this.sort != null) {
            if (this.sort.contains(",")) {
                String[] properties = this.sort.split(",");
                if (properties[1].equalsIgnoreCase("asc"))
                    return Sort.Direction.Ascending;
                if (properties[1].equalsIgnoreCase("desc"))
                    return Sort.Direction.Descending;
            }
        }
        return Sort.Direction.Ascending;
    }
}
