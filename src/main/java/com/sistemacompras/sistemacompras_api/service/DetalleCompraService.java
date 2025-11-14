// com.sistemacompras.sistemacompras_api.service.DetalleCompraService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;

import java.util.List;

public interface DetalleCompraService {
    List<DetalleCompraResponseDto> findAll();
    DetalleCompraResponseDto findById(Long id);
    DetalleCompraResponseDto create(DetalleCompraRequestDto dto);
    DetalleCompraResponseDto update(Long id, DetalleCompraRequestDto dto);
    void delete(Long id);


}