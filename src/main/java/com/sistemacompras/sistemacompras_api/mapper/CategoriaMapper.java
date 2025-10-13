// com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Categoria;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria toEntity(CategoriaRequestDto req);
    CategoriaResponseDto toResponse(Categoria entity);
    List<CategoriaResponseDto> toResponseList(List<Categoria> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoriaRequestDto req, @MappingTarget Categoria entity);
}

