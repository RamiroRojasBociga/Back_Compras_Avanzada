package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompraMapper {

    @Mapping(source = "idUsuario", target = "usuario.idUsuario")
    @Mapping(source = "idProveedor", target = "proveedor.idProveedor")
    Compra toEntity(CompraRequestDto dto);

    @Mapping(source = "usuario.idUsuario", target = "idUsuario")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "proveedor.idProveedor", target = "idProveedor")
    @Mapping(source = "proveedor.nombre", target = "nombreProveedor")
    CompraResponseDto toResponse(Compra entity);

    // Método para convertir lista de entidades a lista de DTOs
    List<CompraResponseDto> toResponseList(List<Compra> entities);

    // Método para actualizar entidad desde DTO (ignorando el ID)
    @Mapping(target = "idCompra", ignore = true)
    @Mapping(source = "idUsuario", target = "usuario.idUsuario")
    @Mapping(source = "idProveedor", target = "proveedor.idProveedor")
    void updateEntityFromRequest(CompraRequestDto dto, @MappingTarget Compra entity);
}
