// com.sistemacompras.sistemacompras_api.service.ProveedorService
package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;

import java.util.List;

public interface ProveedorService {
    List<ProveedorResponseDto> findAll();
    ProveedorResponseDto findById(Long id);
    ProveedorResponseDto create(ProveedorRequestDto dto);
    ProveedorResponseDto update(Long id, ProveedorRequestDto dto);
    void delete(Long id);


}
