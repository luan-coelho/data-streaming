package br.com.unitins.rest.resource;

import br.com.unitins.mapper.task.TaskMapper;
import br.com.unitins.rest.dto.task.TaskResponseDTO;
import br.com.unitins.service.task.TaskService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
