package br.com.unitins.mapper.task;

import br.com.unitins.domain.model.task.Task;
import br.com.unitins.rest.dto.task.TaskResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskResponseDTO toResponseDto(Task task);
}
