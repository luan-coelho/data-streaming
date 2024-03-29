package br.com.unitins.dto.video;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
public class VideoCreateDTO {

    @NotBlank(message = "Enter the title of the video")
    private String title;
    @NotBlank(message = "Enter the description of the video")
    private String description;
}
