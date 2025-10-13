// com.sistemacompras.sistemacompras_api.service.CompraService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;

import java.util.List;

public interface CompraService {
    List<CompraResponseDto> findAll();
    CompraResponseDto findById(Long id);
    CompraResponseDto create(CompraRequestDto dto);
    CompraResponseDto update(Long id, CompraRequestDto dto);
    void delete(Long id);
}
