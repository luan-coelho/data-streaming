package br.com.unitins.rest.resource.dto.video;

import br.com.unitins.rest.resource.ResolutionPathResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class VideoResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String path;
    private List<ResolutionPathResponseDTO> resolutionPaths;
}
