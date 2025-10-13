// com.sistemacompras.sistemacompras_api.service.impl.CategoriaServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.UnidadMedida;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.UnidadMedidaMapper;
import com.sistemacompras.sistemacompras_api.repository.UnidadMedidaRepository;
import com.sistemacompras.sistemacompras_api.service.UnidadMedidaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private final UnidadMedidaRepository repo;
    private final UnidadMedidaMapper mapper;

    public UnidadMedidaServiceImpl(UnidadMedidaRepository repo, UnidadMedidaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<UnidadMedidaResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public UnidadMedidaResponseDto findById(Long id) {
        UnidadMedida e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad Medida " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public UnidadMedidaResponseDto create(UnidadMedidaRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la unidad de medida  ya existe");
        }
        UnidadMedida saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public UnidadMedidaResponseDto update(Long id, UnidadMedidaRequestDto dto) {
        UnidadMedida e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidad de Medida " + id + " no encontrada"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la Unidad de Medida ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Unidad de Medida " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
