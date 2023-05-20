package br.com.unitins.resource;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.dto.video.VideoCreateDTO;
import br.com.unitins.dto.video.VideoUpdateDTO;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.mapper.video.VideoMapper;
import br.com.unitins.model.video.Video;
import br.com.unitins.model.video.VideoWatchTime;
import br.com.unitins.queue.VideoProcessing;
import br.com.unitins.service.task.TaskService;
import br.com.unitins.service.video.VideoService;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Blocking
@Path("/video")
@Produces(MediaType.TEXT_HTML)
public class VideoTemplateResource {

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance index();

        public static native TemplateInstance streaming();

        public static native TemplateInstance create();

        public static native TemplateInstance edit();
    }

    @Inject
    VideoService videoService;

    @Inject
    TaskService taskService;

    @Inject
    VideoProcessing videoProcessing;

    @GET
    @Path("/")
    public TemplateInstance index(Pageable pageable, VideoFilter filter) {
        List<Video> videos = videoService.getAll(pageable, filter).getContent();
        return Templates.index().data("videos", videos);
    }

    @GET
    @Path("/{id}")
    public TemplateInstance getById(@RestPath Long id) {
        Video video = videoService.getById(id);
        boolean videoProcessing = taskService.activeTaskVideoById(id);
        return Templates.streaming()
                .data("video", video)
                .data("videoProcessing", videoProcessing);
    }

    @GET
    @Path("/create")
    public TemplateInstance create() {
        return Templates.create();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid VideoCreateDTO videoCreateDTO) {
        Video video = VideoMapper.INSTANCE.toEntity(videoCreateDTO);
        Video videoPersisted = videoService.create(video);
        return Response.ok(videoPersisted).build();
    }

    @GET
    @Path("/edit/{id}")
    public TemplateInstance edit(Long id) {
        Video video = videoService.getById(id);
        return Templates.edit().data("video", video);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Long id, @Valid VideoUpdateDTO videoUpdateDTO) {
        Video video = VideoMapper.INSTANCE.toEntity(videoUpdateDTO);
        Video videoUpdated = videoService.update(id, video);
        return Response.ok(videoUpdated).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@RestPath Long id) {
        videoService.delete(id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deleteResources/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteResourcesById(@RestPath Long id) {
        videoService.deleteResourcesById(id);
        return Response.ok().build();
    }

    @POST
    @Path("/uploud")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@RestQuery("videoid") Long videoId, MultipartBody multipartBody) {
        videoProcessing.startProcess(videoId, multipartBody);
        return Response.ok().build();
    }

    @GET
    @Path("/streaming")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@HeaderParam("Range") String rangeHeader, @RestQuery("videopath") String videoInputPath) throws IOException {
        File resource = videoService.getResourceByPath(videoInputPath);

        if (resource.exists()) {
            byte[] videoBytes = Files.readAllBytes(resource.toPath());
            int videoLength = videoBytes.length;

            String[] rangeParts = rangeHeader.split("=")[1].split("-");
            int start = Integer.parseInt(rangeParts[0]);
            int end = videoLength - 1;
            if (rangeParts.length > 1) {
                end = Integer.parseInt(rangeParts[1]);
            }

            Response.ResponseBuilder responseBuilder = Response.status(206);
            responseBuilder.header("Accept-Ranges", "bytes");
            responseBuilder.header("Content-Range", "bytes " + start + "-" + end + "/" + videoLength);
            responseBuilder.header("Content-Length", end - start + 1);
            responseBuilder.entity(new ByteArrayInputStream(Arrays.copyOfRange(videoBytes, start, end + 1)));

            return responseBuilder.build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/incrementView")
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementView(@RestQuery("videoid") Long videoId) {
        videoService.incrementViews(videoId);
        return Response.ok().build();
    }

    @POST
    @Path("/updateWatchTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWatchTime(VideoWatchTime videoWatchTime) {
        videoService.updateWatchTime(videoWatchTime);
        return Response.ok().build();
    }

    @GET
    @Path("/getWatchTime/{videoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWatchTime(@RestPath Long videoId) {
        double watchTime = videoService.getWatchTime(videoId);
        return Response.ok(watchTime).build();
    }
}
