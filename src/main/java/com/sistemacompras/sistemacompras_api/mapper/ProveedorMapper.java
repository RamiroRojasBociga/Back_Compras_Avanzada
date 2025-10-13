// com.sistemacompras.sistemacompras_api.mapper.ProveedorMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {
    Proveedor toEntity(ProveedorRequestDto req);
    ProveedorResponseDto toResponse(Proveedor entity);
    List<ProveedorResponseDto> toResponseList(List<Proveedor> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProveedorRequestDto req, @MappingTarget Proveedor entity);
}

