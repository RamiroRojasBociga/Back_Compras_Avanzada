// com.sistemacompras.sistemacompras_api.service.CiudadService
package com.sistemacompras.sistemacompras_api.service;



import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;

import java.util.List;

public interface CiudadService {
    List<CiudadResponseDto> findAll();
    CiudadResponseDto findById(Long id);
    CiudadResponseDto create(CiudadRequestDto dto);
    CiudadResponseDto update(Long id, CiudadRequestDto dto);
    void delete(Long id);

    interface UsuarioService {
        List<UsuarioResponseDto> findAll();
        UsuarioResponseDto findById(Long id);
        UsuarioResponseDto create(UsuarioRequestDto dto);
        UsuarioResponseDto update(Long id, UsuarioRequestDto dto);
        void delete(Long id);
    }
}
