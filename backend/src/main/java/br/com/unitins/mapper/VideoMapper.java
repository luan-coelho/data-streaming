package br.com.unitins.mapper;

import br.com.unitins.domain.Video;
import br.com.unitins.rest.resource.dto.video.VideoCreateDTO;
import br.com.unitins.rest.resource.dto.video.VideoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

@Mapper
public interface VideoMapper {

    @Singleton
    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);
    ResolutionPathMapper RESOLUTION_PATH_MAPPER = ResolutionPathMapper.INSTANCE;

    @Mapping(source = "video.resolutionPaths", target = "resolutionPaths")
    VideoResponseDTO toResponseDto(Video video);

    VideoResponseDTO toResponseDTO(Video video);

    Video toEntity(VideoCreateDTO dto);
}
