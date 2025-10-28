// com.sistemacompras.sistemacompras_api.mapper.DetalleCompraMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {

    @Mapping(source = "idCompra", target = "compra.idCompra")
    @Mapping(source = "idProducto", target = "producto.idProducto")
    DetalleCompra toEntity(DetalleCompraRequestDto req);

    @Mapping(source = "compra.idCompra", target = "idCompra")
    @Mapping(source = "compra.usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "producto.idProducto", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    DetalleCompraResponseDto toResponse(DetalleCompra entity);

    List<DetalleCompraResponseDto> toResponseList(List<DetalleCompra> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "compra", ignore = true)
    @Mapping(target = "producto", ignore = true)
    void updateEntityFromRequest(DetalleCompraRequestDto req, @MappingTarget DetalleCompra entity);
}

