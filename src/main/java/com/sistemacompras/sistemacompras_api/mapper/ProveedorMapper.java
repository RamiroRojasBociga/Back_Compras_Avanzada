package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProveedorMapper {

    @Mapping(target = "ciudad.id", source = "idCiudad")
    @Mapping(target = "telefonos", ignore = true) // Se maneja en el servicio
    Proveedor toEntity(ProveedorRequestDto dto);

    @Mapping(source = "ciudad.id", target = "idCiudad")
    @Mapping(source = "ciudad.nombre", target = "nombreCiudad")
    @Mapping(source = "telefonos", target = "telefonos", qualifiedByName = "mapTelefonos")
    ProveedorResponseDto toResponse(Proveedor entity);

    List<ProveedorResponseDto> toResponseList(List<Proveedor> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ciudad", ignore = true)
    @Mapping(target = "telefonos", ignore = true)
    void updateEntityFromRequest(ProveedorRequestDto dto, @MappingTarget Proveedor entity);

    @Named("mapTelefonos")
    default List<String> mapTelefonos(List<ProveedorTelefono> proveedorTelefonos) {
        if (proveedorTelefonos == null) return List.of();
        return proveedorTelefonos.stream()
                .map(pt -> pt.getTelefono().getNumero())
                .collect(Collectors.toList());
    }
}