package br.com.unitins.dto.video;

import br.com.unitins.dto.video.resolution.ResolutionPathResponseDTO;
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
    private long duration;
    private int views = 0;
    private List<ResolutionPathResponseDTO> resolutionPaths;
}
