package br.com.unitins.resource;

import br.com.unitins.config.AppConfig;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/environment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnvironmentResource {

    @Inject
    AppConfig appConfig;

    @GET
    @Path("/active")
    public Response getActive() {
        return Response.ok(appConfig.getEnvironment()).build();
    }
}
