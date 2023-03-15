package br.com.unitins.rest.dto.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import br.com.unitins.rest.dto.video.VideoResponseSimpleDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskResponseDTO {

    private Long id;
    private TaskStatus status;
    private VideoResponseSimpleDTO video;
}
