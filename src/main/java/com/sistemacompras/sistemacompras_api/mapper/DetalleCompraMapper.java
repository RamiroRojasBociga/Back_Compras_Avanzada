// com.sistemacompras.sistemacompras_api.mapper.DetalleCompraMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetalleCompraMapper {

    // Mapear DTO de request a entidad
    @Mapping(source = "idCompra", target = "compra.idCompra")
    @Mapping(source = "idProducto", target = "producto.idProducto")
    DetalleCompra toEntity(DetalleCompraRequestDto req);

    // Mapear entidad a DTO de respuesta
    @Mapping(source = "idDetalleCompra", target = "idDetalleCompra")          // <--- NUEVO
    @Mapping(source = "compra.idCompra", target = "idCompra")
    @Mapping(source = "compra.usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "producto.idProducto", target = "idProducto")
    @Mapping(source = "producto.nombre", target = "nombreProducto")
    @Mapping(target = "precioUnitario", expression = "java(convertirPrecio(entity))")
    DetalleCompraResponseDto toResponse(DetalleCompra entity);

    // Lista de entidades a lista de DTO
    List<DetalleCompraResponseDto> toResponseList(List<DetalleCompra> entities);

    // Actualizar entidad desde request, ignorando relaciones
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "compra", ignore = true)
    @Mapping(target = "producto", ignore = true)
    void updateEntityFromRequest(DetalleCompraRequestDto req, @MappingTarget DetalleCompra entity);

    // Convierte el precio BigDecimal del producto a Double para el DTO
    default Double convertirPrecio(DetalleCompra entity) {
        if (entity == null || entity.getProducto() == null || entity.getProducto().getPrecio() == null) {
            return 0.0;
        }
        return entity.getProducto().getPrecio().doubleValue();
    }
}
