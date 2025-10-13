// com.sistemacompras.sistemacompras_api.mapper.CompraMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompraMapper {
    Compra toEntity(CompraRequestDto req);
    CompraResponseDto toResponse(Compra entity);
    List<CompraResponseDto> toResponseList(List<Compra> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CompraRequestDto req, @MappingTarget Compra entity);
}

