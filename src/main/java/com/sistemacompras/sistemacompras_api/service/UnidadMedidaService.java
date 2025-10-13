// com.sistemacompras.sistemacompras_api.service.CategoriaService
package com.sistemacompras.sistemacompras_api.service;


import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;

import java.util.List;

public interface UnidadMedidaService {
    List<UnidadMedidaResponseDto> findAll();
    UnidadMedidaResponseDto findById(Long id);
    UnidadMedidaResponseDto create(UnidadMedidaRequestDto dto);
    UnidadMedidaResponseDto update(Long id, UnidadMedidaRequestDto dto);
    void delete(Long id);
}
