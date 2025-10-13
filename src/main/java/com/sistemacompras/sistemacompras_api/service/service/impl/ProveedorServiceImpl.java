// com.sistemacompras.sistemacompras_api.service.impl.ProveedorServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ProveedorMapper;
import com.sistemacompras.sistemacompras_api.repository.ProveedorRepository;
import com.sistemacompras.sistemacompras_api.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repo;
    private final ProveedorMapper mapper;

    public ProveedorServiceImpl(ProveedorRepository repo, ProveedorMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDto findById(Long id) {
        Proveedor e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public ProveedorResponseDto create(ProveedorRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de categoría ya existe");
        }
        Proveedor saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public ProveedorResponseDto update(Long id, ProveedorRequestDto dto) {
        Proveedor e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor " + id + " no encontrada"));

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
            throw new ResourceNotFoundException("Proveedor " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
