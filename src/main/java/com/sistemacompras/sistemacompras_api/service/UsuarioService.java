// com.sistemacompras.sistemacompras_api.service.UsuarioService
package com.sistemacompras.sistemacompras_api.service;


import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDto> findAll();
    UsuarioResponseDto findById(Long id);
    UsuarioResponseDto create(UsuarioRequestDto dto);
    UsuarioResponseDto update(Long id, UsuarioRequestDto dto);
    UsuarioResponseDto findByEmail(String email);
    Usuario findEntityByEmail(String email);
    void delete(Long id);



}
