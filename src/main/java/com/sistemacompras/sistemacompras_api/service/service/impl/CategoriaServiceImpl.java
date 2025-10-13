// com.sistemacompras.sistemacompras_api.service.impl.CategoriaServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Categoria;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CategoriaMapper;
import com.sistemacompras.sistemacompras_api.repository.CategoriaRepository;
import com.sistemacompras.sistemacompras_api.service.CategoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repo;
    private final CategoriaMapper mapper;

    public CategoriaServiceImpl(CategoriaRepository repo, CategoriaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDto findById(Long id) {
        Categoria e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public CategoriaResponseDto create(CategoriaRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de categoría ya existe");
        }
        Categoria saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public CategoriaResponseDto update(Long id, CategoriaRequestDto dto) {
        Categoria e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria " + id + " no encontrada"));

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
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
