// com.sistemacompras.sistemacompras_api.service.TelefonoService
package com.sistemacompras.sistemacompras_api.service;



import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;

import java.util.List;

public interface TelefonoService {
    List<TelefonoResponseDto> findAll();
    TelefonoResponseDto findById(Long id);
    TelefonoResponseDto create(TelefonoRequestDto dto);
    TelefonoResponseDto update(Long id, TelefonoRequestDto dto);
    void delete(Long id);
}
