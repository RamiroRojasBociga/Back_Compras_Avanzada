// com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper
package com.sistemacompras.sistemacompras_api.mapper;


import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Marca;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MarcaMapper {
    Marca toEntity(MarcaRequestDto req);
    MarcaResponseDto toResponse(Marca entity);
    List<MarcaResponseDto> toResponseList(List<Marca> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(MarcaRequestDto req, @MappingTarget Marca entity);
}

