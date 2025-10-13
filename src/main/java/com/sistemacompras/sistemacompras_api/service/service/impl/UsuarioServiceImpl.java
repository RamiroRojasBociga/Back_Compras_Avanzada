// com.sistemacompras.sistemacompras_api.service.impl.UsuarioServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;


import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;

import com.sistemacompras.sistemacompras_api.mapper.UsuarioMapper;
import com.sistemacompras.sistemacompras_api.repository.UsuarioRepository;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;

    public UsuarioServiceImpl(UsuarioRepository repo, UsuarioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }
    public Usuario findEntityByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }


    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto findById(Long id) {
        Usuario e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario " + id + " no encontrada"));
        return mapper.toResponse(e);
    }
    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto findByEmail(String email) {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email '" + email + "' no encontrado"));
        return mapper.toResponse(usuario);
    }

    public UsuarioResponseDto create(UsuarioRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de categoría ya existe");
        }
        Usuario saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public UsuarioResponseDto update(Long id, UsuarioRequestDto dto) {
        Usuario e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario " + id + " no encontrada"));

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
            throw new ResourceNotFoundException("Usuario " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
