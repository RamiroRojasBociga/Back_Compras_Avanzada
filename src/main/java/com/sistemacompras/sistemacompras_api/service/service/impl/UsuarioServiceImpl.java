// com.sistemacompras.sistemacompras_api.service.impl.UsuarioServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;


import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;

import com.sistemacompras.sistemacompras_api.mapper.UsuarioMapper;
import com.sistemacompras.sistemacompras_api.repository.UsuarioRepository;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder; // NUEVO

    // Constructor actualizado con PasswordEncoder
    public UsuarioServiceImpl(UsuarioRepository repo, UsuarioMapper mapper,
                              PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder; // NUEVO
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
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Convertimos el DTO a entidad
        Usuario usuario = mapper.toEntity(dto);

        // NUEVO: Encriptamos la contraseña ANTES de guardar
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Guardamos con la contraseña ya encriptada
        Usuario saved = repo.save(usuario);
        return mapper.toResponse(saved);
    }

    public UsuarioResponseDto update(Long id, UsuarioRequestDto dto) {
        Usuario e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario " + id + " no encontrado"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);

        // NUEVO: Si se actualiza la contraseña, la encriptamos
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            e.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Usuario " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
