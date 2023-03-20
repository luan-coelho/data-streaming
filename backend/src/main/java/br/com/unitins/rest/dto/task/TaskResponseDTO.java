package br.com.unitins.rest.dto.task;

import br.com.unitins.domain.enums.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TaskResponseDTO {

    private Long id;
    private TaskStatus status;
    private String details;
    private LocalDateTime localDateTime;
    private Long videoId;
}
