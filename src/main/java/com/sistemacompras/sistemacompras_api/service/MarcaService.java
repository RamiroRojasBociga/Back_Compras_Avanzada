// com.sistemacompras.sistemacompras_api.service.CategoriaService
package com.sistemacompras.sistemacompras_api.service;


import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;

import java.util.List;

public interface MarcaService {
    List<MarcaResponseDto> findAll();
    MarcaResponseDto findById(Long id);
    MarcaResponseDto create(MarcaRequestDto dto);
    MarcaResponseDto update(Long id, MarcaRequestDto dto);
    void delete(Long id);
}
