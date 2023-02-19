package br.com.unitins.rest.resource;


import br.com.unitins.domain.enums.Resolution;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResolutionPathResponseDTO {

    private Long id;
    private Resolution resolution;
    private String path;
}
