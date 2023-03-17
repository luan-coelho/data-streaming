package br.com.unitins.commons.pagination;

import br.com.unitins.commons.pagination.Pagination;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.QueryParam;
import java.lang.reflect.Field;

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
     * Campo para ser ordenado
     *
     * @return Campo
     */
    public String getSort(Class<?> clazz) {
        if (this.sort != null) {
            if (this.sort.contains(",")) {
                String[] properties = this.sort.split(",");
                if (existsField(clazz, properties[0])) {
                    return properties[0];
                }
            }
        }
        this.sort = getRandomFieldName(clazz);
        return sort;
    }

    /**
     * Ordem para paginação
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

    /**
     * Verifica em tempo de execução se o campo passado por query params existe na entidade. Caso não existe é definido
     *
     * @param clazz Classe
     * @param fieldName Nome do campo
     * @return Verdadeiro se o campo passado como argumento existir na classe
     */
    boolean existsField(Class<?> clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pega um nome de qualquer campo da classe
     * @param clazz Classe
     * @return Campo aleatorio da classe
     */
    String getRandomFieldName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isArray()) {
                continue;
            }
            return field.getName();
        }
        return "";
    }
}
