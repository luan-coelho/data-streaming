package br.com.unitins.rest.dto.video;

import br.com.unitins.rest.dto.video.resolution.ResolutionPathResponseDTO;
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
