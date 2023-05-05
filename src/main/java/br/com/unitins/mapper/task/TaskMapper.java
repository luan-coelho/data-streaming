package br.com.unitins.mapper.task;

import br.com.unitins.dto.task.TaskResponseDTO;
import br.com.unitins.model.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskResponseDTO toResponseDto(Task task);
}
