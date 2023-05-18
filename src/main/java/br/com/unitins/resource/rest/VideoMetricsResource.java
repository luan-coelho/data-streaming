package br.com.unitins.resource.rest;

import br.com.unitins.model.video.VideoMetrics;
import br.com.unitins.service.video.VideoMetricsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

import java.time.LocalDate;
import java.util.List;

@Path("/api/video-metrics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoMetricsResource {

    @Inject
    VideoMetricsService videoMetricsService;

    @GET
    @Path("/increment-view/{videoId}")
    public Response incrementViewsByDate(@RestPath Long videoId) {
        videoMetricsService.incrementViewsByDate(videoId);
        return Response.ok().build();
    }

    @GET
    @Path("/top")
    public Response incrementViewsByDate(@QueryParam(value = "date") String date) {
        List<VideoMetrics> videos = videoMetricsService.listTopVideosByDate(LocalDate.parse(date));
        return Response.ok(videos).build();
    }
}
