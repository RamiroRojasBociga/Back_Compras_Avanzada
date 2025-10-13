// com.sistemacompras.sistemacompras_api.service.ProductoService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;

import java.util.List;

public interface ProductoService {
    List<ProductoResponseDto> findAll();
    ProductoResponseDto findById(Long id);
    ProductoResponseDto create(ProductoRequestDto dto);
    ProductoResponseDto update(Long id, ProductoRequestDto dto);
    void delete(Long id);

    // NUEVOS MÉTODOS CON ENUM
    List<ProductoResponseDto> findByEstado(EstadoProducto estado);
    void cambiarEstado(Long id, EstadoProducto nuevoEstado);
    List<ProductoResponseDto> findProductosActivos();
}
