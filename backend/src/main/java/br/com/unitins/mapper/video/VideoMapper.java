package br.com.unitins.mapper.video;

import br.com.unitins.model.video.Video;
import br.com.unitins.dto.video.VideoCreateDTO;
import br.com.unitins.dto.video.VideoResponseDTO;
import br.com.unitins.dto.video.VideoUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    VideoResponseDTO toResponseDto(Video video);

    Video toEntity(VideoCreateDTO dto);

    Video toEntity(VideoUpdateDTO dto);

    @Mapping(target = "id", ignore = true)
    void copyFields(@MappingTarget Video source, Video target);
}
