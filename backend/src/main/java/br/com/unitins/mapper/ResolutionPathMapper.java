package br.com.unitins.mapper;

import br.com.unitins.domain.ResolutionPath;
import br.com.unitins.rest.resource.ResolutionPathResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

@Mapper
public interface ResolutionPathMapper {

    @Singleton
    ResolutionPathMapper INSTANCE = Mappers.getMapper(ResolutionPathMapper.class);

    ResolutionPathResponseDTO toResponseDto(ResolutionPath resolutionPath);
}
