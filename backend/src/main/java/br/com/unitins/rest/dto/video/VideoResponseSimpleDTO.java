package br.com.unitins.rest.dto.video;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoResponseSimpleDTO {

    private Long id;
    private String title;
    private String description;
}
