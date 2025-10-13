// com.sistemacompras.sistemacompras_api.mapper.UsuarioMapper
package com.sistemacompras.sistemacompras_api.mapper;


import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toEntity(UsuarioRequestDto req);
    UsuarioResponseDto toResponse(Usuario entity);
    List<UsuarioResponseDto> toResponseList(List<Usuario> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UsuarioRequestDto req, @MappingTarget Usuario entity);
}

