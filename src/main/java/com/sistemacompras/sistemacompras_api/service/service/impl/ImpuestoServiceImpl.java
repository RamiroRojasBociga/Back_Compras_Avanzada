// com.sistemacompras.sistemacompras_api.service.impl.ImpuestoServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;


import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Impuesto;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;

import com.sistemacompras.sistemacompras_api.mapper.ImpuestoMapper;
import com.sistemacompras.sistemacompras_api.repository.ImpuestoRepository;
import com.sistemacompras.sistemacompras_api.service.ImpuestoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImpuestoServiceImpl implements ImpuestoService {

    private final ImpuestoRepository repo;
    private final ImpuestoMapper mapper;

    public ImpuestoServiceImpl(ImpuestoRepository repo, ImpuestoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ImpuestoResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public ImpuestoResponseDto findById(Long id) {
        Impuesto e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Impuesto " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public ImpuestoResponseDto create(ImpuestoRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del impuesto ya existe");
        }
        Impuesto saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public ImpuestoResponseDto update(Long id, ImpuestoRequestDto dto) {
        Impuesto e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Impuesto " + id + " no encontrada"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de categoría ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Impuesto " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
