// com.sistemacompras.sistemacompras_api.service.ImpuestoService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;

import java.util.List;

public interface ImpuestoService {
    List<ImpuestoResponseDto> findAll();
    ImpuestoResponseDto findById(Long id);
    ImpuestoResponseDto create(ImpuestoRequestDto dto);
    ImpuestoResponseDto update(Long id, ImpuestoRequestDto dto);
    void delete(Long id);
}
