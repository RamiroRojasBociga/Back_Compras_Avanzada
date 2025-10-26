package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "categoria.id", source = "idCategoria")
    @Mapping(target = "marca.id", source = "idMarca")
    @Mapping(target = "unidadMedida.id", source = "idUnidadMedida")
    @Mapping(target = "impuesto.id", source = "idImpuesto")
    Producto toEntity(ProductoRequestDto req);

    @Mapping(source = "categoria.id", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    @Mapping(source = "marca.id", target = "idMarca")
    @Mapping(source = "marca.nombre", target = "nombreMarca")
    @Mapping(source = "unidadMedida.id", target = "idUnidadMedida")
    @Mapping(source = "unidadMedida.nombre", target = "nombreUnidadMedida")
    @Mapping(source = "impuesto.id", target = "idImpuesto")
    @Mapping(source = "impuesto.nombre", target = "nombreImpuesto")
    @Mapping(source = "impuesto.porcentaje", target = "porcentajeImpuesto")
    ProductoResponseDto toResponse(Producto entity);

    List<ProductoResponseDto> toResponseList(List<Producto> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "unidadMedida", ignore = true)
    @Mapping(target = "impuesto", ignore = true)
    void updateEntityFromRequest(ProductoRequestDto req, @MappingTarget Producto entity);
}