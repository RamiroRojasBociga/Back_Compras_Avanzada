// com.sistemacompras.sistemacompras_api.mapper.ImpuestoMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Impuesto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImpuestoMapper {
    Impuesto toEntity(ImpuestoRequestDto req);
    ImpuestoResponseDto toResponse(Impuesto entity);
    List<ImpuestoResponseDto> toResponseList(List<Impuesto> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ImpuestoRequestDto req, @MappingTarget Impuesto entity);
}

