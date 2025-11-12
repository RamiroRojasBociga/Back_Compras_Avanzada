package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioUpdateDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponseDto> findAll();
    UsuarioResponseDto findById(Long id);
    UsuarioResponseDto create(UsuarioRequestDto dto);
    UsuarioResponseDto update(Long id, UsuarioUpdateDto dto);  // Usar UsuarioUpdateDto
    UsuarioResponseDto findByEmail(String email);
    Usuario findEntityByEmail(String email);
    void delete(Long id);
}
