package br.com.unitins.resource;

import br.com.unitins.mapper.task.TaskMapper;
import br.com.unitins.dto.task.TaskResponseDTO;
import br.com.unitins.service.task.TaskService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/task")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    @GET
    public Response getAll() {
        List<TaskResponseDTO> dto = taskService.getAll().stream().map(TaskMapper.INSTANCE::toResponseDto).collect(Collectors.toList());
        return Response.ok(dto).build();
    }

    @GET
    @Path("/active")
    public Response getActive() {
        List<TaskResponseDTO> dto = taskService.getActive().stream().map(TaskMapper.INSTANCE::toResponseDto).collect(Collectors.toList());
        return Response.ok(dto).build();
    }
}
