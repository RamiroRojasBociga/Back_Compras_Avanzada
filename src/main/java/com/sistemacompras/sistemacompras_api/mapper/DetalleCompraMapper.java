// com.sistemacompras.sistemacompras_api.mapper.DetalleCompraMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {
    DetalleCompra toEntity(DetalleCompraRequestDto req);
    DetalleCompraResponseDto toResponse(DetalleCompra entity);
    List<DetalleCompraResponseDto> toResponseList(List<DetalleCompra> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(DetalleCompraRequestDto req, @MappingTarget DetalleCompra entity);
}

