package br.com.unitins.rest.resource;

import br.com.unitins.domain.model.video.VideoMetrics;
import br.com.unitins.service.video.VideoMetricsService;
import org.jboss.logging.annotations.Param;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
