// ==========================================================
// com.sistemacompras.sistemacompras_api.mapper.ProductoMapper
// ==========================================================
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // 🔹 Mapea los IDs del DTO hacia los objetos relacionados usando su campo "id"
    @Mapping(target = "categoria.id", source = "idCategoria")
    @Mapping(target = "marca.id", source = "idMarca")
    @Mapping(target = "unidadMedida.id", source = "idUnidadMedida")
    @Mapping(target = "impuesto.id", source = "idImpuesto")
    Producto toEntity(ProductoRequestDto req);

    // 🔹 Convierte la entidad a DTO de respuesta
    ProductoResponseDto toResponse(Producto entity);

    // 🔹 Convierte una lista de entidades a lista de DTOs
    List<ProductoResponseDto> toResponseList(List<Producto> entities);

    // 🔹 Permite actualizar una entidad sin sobrescribir valores nulos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "unidadMedida", ignore = true)
    @Mapping(target = "impuesto", ignore = true)
    void updateEntityFromRequest(ProductoRequestDto req, @MappingTarget Producto entity);
}

