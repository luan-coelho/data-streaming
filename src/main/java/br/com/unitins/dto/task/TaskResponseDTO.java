package br.com.unitins.dto.task;

import br.com.unitins.model.enums.task.TaskStatus;
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
