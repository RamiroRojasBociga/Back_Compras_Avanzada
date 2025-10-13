// com.sistemacompras.sistemacompras_api.mapper.CiudadMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Ciudad;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CiudadMapper {
    Ciudad toEntity(CiudadRequestDto req);
    CiudadResponseDto toResponse(Ciudad entity);
    List<CiudadResponseDto> toResponseList(List<Ciudad> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CiudadRequestDto req, @MappingTarget Ciudad entity);
}

