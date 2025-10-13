// com.sistemacompras.sistemacompras_api.service.impl.CategoriaServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Marca;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.MarcaMapper;
import com.sistemacompras.sistemacompras_api.repository.MarcaRepository;
import com.sistemacompras.sistemacompras_api.service.MarcaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository repo;
    private final MarcaMapper mapper;

    public MarcaServiceImpl(MarcaRepository repo, MarcaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<MarcaResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public MarcaResponseDto findById(Long id) {
        Marca e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public MarcaResponseDto create(MarcaRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la marca ya existe");
        }
        Marca saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public MarcaResponseDto update(Long id, MarcaRequestDto dto) {
        Marca e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca " + id + " no encontrada"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de la marca ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Marca " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
