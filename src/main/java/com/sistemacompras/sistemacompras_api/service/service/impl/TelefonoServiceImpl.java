package com.sistemacompras.sistemacompras_api.service.impl; // Corrige el paquete

import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.TelefonoMapper;
import com.sistemacompras.sistemacompras_api.repository.TelefonoRepository;
import com.sistemacompras.sistemacompras_api.service.TelefonoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TelefonoServiceImpl implements TelefonoService {

    private final TelefonoRepository repo;
    private final TelefonoMapper mapper;

    public TelefonoServiceImpl(TelefonoRepository repo, TelefonoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<TelefonoResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public TelefonoResponseDto findById(Long id) {
        Telefono e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teléfono " + id + " no encontrado"));
        return mapper.toResponse(e);
    }

    public TelefonoResponseDto create(TelefonoRequestDto dto) {
        if (repo.existsByNumero(dto.getNumero())) { // ✅ QUITAR IgnoreCase
            throw new IllegalArgumentException("El número de teléfono ya existe");
        }
        Telefono saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public TelefonoResponseDto update(Long id, TelefonoRequestDto dto) {
        Telefono e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teléfono " + id + " no encontrado"));

        if (dto.getNumero() != null
                && !dto.getNumero().equals(e.getNumero()) // ✅ QUITAR equalsIgnoreCase
                && repo.existsByNumero(dto.getNumero())) { // ✅ QUITAR IgnoreCase
            throw new IllegalArgumentException("El número de teléfono ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Teléfono " + id + " no encontrado");
        }
        repo.deleteById(id);
    }
}