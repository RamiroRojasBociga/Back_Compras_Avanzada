// com.sistemacompras.sistemacompras_api.service.impl.CiudadServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Ciudad;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CiudadMapper;
import com.sistemacompras.sistemacompras_api.repository.CiudadRepository;
import com.sistemacompras.sistemacompras_api.service.CiudadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CiudadServiceImpl implements CiudadService {

    private final CiudadRepository repo;
    private final CiudadMapper mapper;

    public CiudadServiceImpl(CiudadRepository repo, CiudadMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CiudadResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public CiudadResponseDto findById(Long id) {
        Ciudad e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public CiudadResponseDto create(CiudadRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la Ciudad ya existe");
        }
        Ciudad saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public CiudadResponseDto update(Long id, CiudadRequestDto dto) {
        Ciudad e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciudad " + id + " no encontrada"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la ciudad ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Ciudad " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
