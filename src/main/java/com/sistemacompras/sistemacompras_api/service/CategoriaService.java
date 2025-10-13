// com.sistemacompras.sistemacompras_api.service.CategoriaService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponseDto> findAll();
    CategoriaResponseDto findById(Long id);
    CategoriaResponseDto create(CategoriaRequestDto dto);
    CategoriaResponseDto update(Long id, CategoriaRequestDto dto);
    void delete(Long id);
}
