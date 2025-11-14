// com.sistemacompras.sistemacompras_api.service.DetalleCompraService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;

import java.util.List;

public interface DetalleCompraService {

    // Listar todos los detalles de compra
    List<DetalleCompraResponseDto> findAll();

    // Buscar un detalle por su id
    DetalleCompraResponseDto findById(Long id);

    // Crear un nuevo detalle
    DetalleCompraResponseDto create(DetalleCompraRequestDto dto);

    // Actualizar un detalle existente
    DetalleCompraResponseDto update(Long id, DetalleCompraRequestDto dto);

    // Eliminar un detalle por id
    void delete(Long id);

    // NUEVO: listar todos los detalles de una compra específica
    List<DetalleCompraResponseDto> findByCompra(Long idCompra);

}
