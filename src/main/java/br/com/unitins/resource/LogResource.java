package br.com.unitins.resource;

import br.com.unitins.model.log.Log;
import br.com.unitins.service.log.LogService;
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
public class LogResource {

    @Inject
    LogService logService;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance index();
    }

    @GET
    @Path("/")
    public TemplateInstance index() {
        List<Log> logs = logService.getAll();
        return Templates.index().data("logs", logs);
    }
}
