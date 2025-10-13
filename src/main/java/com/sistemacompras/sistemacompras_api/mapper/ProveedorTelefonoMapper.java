package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProveedorTelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorTelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProveedorTelefonoMapper {

    // Convierte DTO de request a entidad
    ProveedorTelefono toEntity(ProveedorTelefonoRequestDto req);

    // Convierte entidad a DTO de response
    ProveedorTelefonoResponseDto toResponse(ProveedorTelefono entity);

    // Convierte lista de entidades a lista de DTOs de response
    List<ProveedorTelefonoResponseDto> toResponseList(List<ProveedorTelefono> entities);

    // Actualiza entidad existente con datos del DTO (ignora valores nulos)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProveedorTelefonoRequestDto req, @MappingTarget ProveedorTelefono entity);
}