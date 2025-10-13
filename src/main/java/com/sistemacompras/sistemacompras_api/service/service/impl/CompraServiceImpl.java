package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CompraMapper;
import com.sistemacompras.sistemacompras_api.repository.CompraRepository;
import com.sistemacompras.sistemacompras_api.service.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final CompraRepository repo;
    private final CompraMapper mapper;

    public CompraServiceImpl(CompraRepository repo, CompraMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CompraResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public CompraResponseDto findById(Long id) {
        Compra e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public CompraResponseDto create(CompraRequestDto dto) {
        // No se puede validar unicidad por nombre ya que no existe ese campo
        Compra saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public CompraResponseDto update(Long id, CompraRequestDto dto) {
        Compra e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra " + id + " no encontrada"));

        // No hay validación de nombre porque Compra no lo tiene
        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Compra " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
