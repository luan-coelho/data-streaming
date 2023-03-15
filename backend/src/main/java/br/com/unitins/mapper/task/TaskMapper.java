package br.com.unitins.mapper.task;

import br.com.unitins.queue.Task;
import br.com.unitins.rest.dto.task.TaskResponseDTO;
import org.mapstruct.factory.Mappers;

public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskResponseDTO toResponseDto(Task task);
}
