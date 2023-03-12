package br.com.unitins.rest.dto.video;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class VideoUpdateDTO {

    @NotBlank(message = "Enter the title of the video")
    private String title;
    @NotBlank(message = "Enter the description of the video")
    private String description;
}
