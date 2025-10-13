// com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.UnidadMedida;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnidadMedidaMapper {
    UnidadMedida toEntity(UnidadMedidaRequestDto req);
    UnidadMedidaResponseDto toResponse(UnidadMedida entity);
    List<UnidadMedidaResponseDto> toResponseList(List<UnidadMedida> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UnidadMedidaRequestDto req, @MappingTarget UnidadMedida entity);
}

