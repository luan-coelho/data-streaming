package br.com.unitins.rest.resource;

import br.com.unitins.config.AppConfig;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
