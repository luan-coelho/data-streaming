package br.com.unitins.mapper.video;

import br.com.unitins.domain.model.Video;
import br.com.unitins.mapper.video.resolution.ResolutionPathMapper;
import br.com.unitins.rest.dto.video.VideoCreateDTO;
import br.com.unitins.rest.dto.video.VideoResponseDTO;
import br.com.unitins.rest.dto.video.VideoUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

@Mapper
public interface VideoMapper {

    @Singleton
    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);
    ResolutionPathMapper RESOLUTION_PATH_MAPPER = ResolutionPathMapper.INSTANCE;

    VideoResponseDTO toResponseDto(Video video);

    VideoResponseDTO toResponseDTO(Video video);

    Video toEntity(VideoCreateDTO dto);

    Video toEntity(VideoUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    void copyFields(@MappingTarget Video source, Video target);
}
