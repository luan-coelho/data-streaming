package br.com.unitins.rest.resource;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.domain.Video;
import br.com.unitins.mapper.VideoMapper;
import br.com.unitins.rest.resource.dto.video.VideoCreateDTO;
import br.com.unitins.rest.resource.dto.video.VideoResponseDTO;
import br.com.unitins.service.VideoService;
import org.jboss.logging.annotations.Param;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Path("/api/video")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VideoResource {

    @Inject
    VideoService videoService;

    @GET
    public Response getAll() {
        List<Video> videoList = videoService.getAll();
        List<VideoResponseDTO> dtos = videoList.stream().map(VideoMapper.INSTANCE::toResponseDto).toList();
        return Response.ok(dtos).build();
    }

    @POST
    public Response create(VideoCreateDTO videoCreateDTO) {
        Video video = VideoMapper.INSTANCE.toEntity(videoCreateDTO);
        Video videoPersisted = videoService.create(video);
        return Response.ok(videoPersisted).build();
    }

    @DELETE
    @Path("/{videoId}")
    public Response delete(@RestPath Long videoId) {
        videoService.delete(videoId);
        return Response.ok().build();
    }

    @POST
    @Path("/uploud")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(MultipartBody multipartBody, @RestQuery("videoid") Long videoId) throws IOException {
        // Define o diretório de destino do arquivo
        java.nio.file.Path filePath = videoService.saveFile(multipartBody);

        videoService.adjustResolution(videoId, filePath.toString());
        return Response.ok().build();
    }

    @GET
    @Path("/streaming")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@HeaderParam("Range") String rangeHeader, String videoInputPath) throws IOException {
        String userHome = System.getProperty("user.home");

        File file = new File(userHome + videoInputPath);

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
