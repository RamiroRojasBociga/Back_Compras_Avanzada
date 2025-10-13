// com.sistemacompras.sistemacompras_api.mapper.ProductoMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    Producto toEntity(ProductoRequestDto req);
    ProductoResponseDto toResponse(Producto entity);
    List<ProductoResponseDto> toResponseList(List<Producto> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductoRequestDto req, @MappingTarget Producto entity);
}

