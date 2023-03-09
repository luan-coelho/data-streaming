package br.com.unitins.mapper.video.resolution;

import br.com.unitins.domain.model.ResourcePath;
import br.com.unitins.rest.dto.video.resolution.ResolutionPathResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

@Mapper
public interface ResolutionPathMapper {

    @Singleton
    ResolutionPathMapper INSTANCE = Mappers.getMapper(ResolutionPathMapper.class);

    ResolutionPathResponseDTO toResponseDto(ResourcePath resolutionPath);
}
