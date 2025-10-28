package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import java.util.List;

public interface CompraService {
    List<CompraResponseDto> findAll();
    CompraResponseDto findById(Long id);
    CompraResponseDto create(CompraRequestDto dto);
    CompraResponseDto update(Long id, CompraRequestDto dto);
    void delete(Long id);

    // ✅ AGREGAR ESTOS MÉTODOS
    List<CompraResponseDto> findByEstado(EstadoCompra estado);
    void cambiarEstado(Long id, EstadoCompra nuevoEstado);
    List<CompraResponseDto> findComprasPendientes();
    List<CompraResponseDto> findComprasProcesadas();
}