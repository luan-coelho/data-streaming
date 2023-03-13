package br.com.unitins.commons;

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

    /**
     * Campo para ser ordenado
     *
     * @return Campo
     */
    public String getSort() {
        if (this.sort != null) {
            if (this.sort.contains(",")) {
                String[] properties = this.sort.split(",");
                return properties[0];
            }
        }
        return "id";
    }

    /**
     * Ordem que retornará os objetos da paginação a partir de um campo
     *
     * @return Ordem
     */
    public String getOrder() {
        if (this.sort != null) {
            if (this.sort.contains(",")) {
                String[] properties = this.sort.split(",");
                if (properties[1].equalsIgnoreCase("DESC"))
                    return properties[1].toUpperCase();
            }
        }
        return "ASC";
    }
}
