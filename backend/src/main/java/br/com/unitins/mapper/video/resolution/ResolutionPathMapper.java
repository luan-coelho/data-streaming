package br.com.unitins.mapper.video.resolution;

import br.com.unitins.dto.video.resolution.ResolutionPathResponseDTO;
import br.com.unitins.model.video.ResourcePath;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import jakarta.inject.Singleton;

@Mapper
public interface ResolutionPathMapper {

    @Singleton
    ResolutionPathMapper INSTANCE = Mappers.getMapper(ResolutionPathMapper.class);

    ResolutionPathResponseDTO toResponseDto(ResourcePath resolutionPath);
}
