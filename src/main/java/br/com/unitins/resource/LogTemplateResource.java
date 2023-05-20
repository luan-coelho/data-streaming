package br.com.unitins.resource;

import br.com.unitins.model.enums.task.TaskStatus;
import br.com.unitins.model.log.Log;
import br.com.unitins.model.task.Task;
import br.com.unitins.service.log.LogService;
import br.com.unitins.service.task.TaskService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Blocking
@Path("/log")
@Produces(MediaType.TEXT_HTML)
public class LogTemplateResource {

    @Inject
    LogService logService;

    @Inject
    TaskService taskService;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance index();
    }

    @GET
    @Path("/")
    public TemplateInstance index() {
        List<Log> logs = logService.getAll();
        List<Task> tasks = taskService.getAll();

        TasksLogs tasksLogs = getTasksLogs(tasks);

        return Templates.index()
                .data("logs", logs)
                .data("tasks", tasks)
                .data("countCompleted", tasksLogs.countCompleted())
                .data("countProcessing", tasksLogs.countProcessing())
                .data("countInterrupted", tasksLogs.countInterrupted());
    }

    private static TasksLogs getTasksLogs(List<Task> tasks) {
        int countCompleted = 0;
        int countProcessing = 0;
        int countInterrupted = 0;

        for (Task task : tasks) {
            if (task.getStatus() == TaskStatus.COMPLETED) {
                countCompleted++;
            }

            if (task.getStatus() == TaskStatus.PROCESSING) {
                countProcessing++;
            }

            if (task.getStatus() == TaskStatus.INTERRUPTED) {
                countInterrupted++;
            }
        }
        return new TasksLogs(countCompleted, countProcessing, countInterrupted);
    }

    private record TasksLogs(int countCompleted, int countProcessing, int countInterrupted) {
    }
}
