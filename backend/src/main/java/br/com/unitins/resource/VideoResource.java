package br.com.unitins.resource;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.model.video.Video;
import br.com.unitins.mapper.video.VideoMapper;
import br.com.unitins.queue.VideoProcessing;
import br.com.unitins.dto.video.VideoCreateDTO;
import br.com.unitins.dto.video.VideoResponseDTO;
import br.com.unitins.dto.video.VideoUpdateDTO;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.service.video.VideoService;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Path("/api/video")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    VideoService videoService;

    @Inject
    VideoProcessing videoProcessing;

    @GET
    public Response getAll(Pageable pageable, VideoFilter filter) {
        Pagination<Video> videoList = videoService.getAll(pageable, filter);
        return Response.ok(videoList).build();
    }

    @POST
    public Response create(@Valid VideoCreateDTO videoCreateDTO) {
        Video video = VideoMapper.INSTANCE.toEntity(videoCreateDTO);
        Video videoPersisted = videoService.create(video);
        return Response.ok(videoPersisted).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(Long id, @Valid VideoUpdateDTO videoUpdateDTO) {
        Video video = VideoMapper.INSTANCE.toEntity(videoUpdateDTO);
        Video videoUpdated = videoService.update(id, video);
        return Response.ok(videoUpdated).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@RestPath Long id) {
        Video video = videoService.getById(id);
        VideoResponseDTO dto = VideoMapper.INSTANCE.toResponseDto(video);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@RestPath Long id) {
        videoService.delete(id);
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
        String userHome = System.getProperty("user.home");
        String fullPath = userHome + videoInputPath;
        File file = new File(fullPath);

        byte[] videoBytes = Files.readAllBytes(file.toPath());
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
}
