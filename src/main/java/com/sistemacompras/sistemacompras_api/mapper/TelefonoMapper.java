// com.sistemacompras.sistemacompras_api.mapper.TelefonoMapper
package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefonoMapper {
    Telefono toEntity(TelefonoRequestDto req);
    TelefonoResponseDto toResponse(Telefono entity);
    List<TelefonoResponseDto> toResponseList(List<Telefono> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(TelefonoRequestDto req, @MappingTarget Telefono entity);
}

