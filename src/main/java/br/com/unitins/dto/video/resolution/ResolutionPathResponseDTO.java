package br.com.unitins.dto.video.resolution;

import br.com.unitins.model.enums.video.Resolution;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResolutionPathResponseDTO {

    private Long id;
    private Resolution resolution;
    private String path;
    private long processingTime;
}
