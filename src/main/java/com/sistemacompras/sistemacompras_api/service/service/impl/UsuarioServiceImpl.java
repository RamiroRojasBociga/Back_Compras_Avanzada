// com.sistemacompras.sistemacompras_api.service.impl.UsuarioServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioUpdateDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.UsuarioMapper;
import com.sistemacompras.sistemacompras_api.repository.UsuarioRepository;
import com.sistemacompras.sistemacompras_api.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementación del servicio de usuarios con lógica de negocio y encriptación de passwords
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    // Constructor con inyección de dependencias
    public UsuarioServiceImpl(UsuarioRepository repo, UsuarioMapper mapper,
                              PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Busca un usuario por email y retorna la entidad completa (para uso interno)
    public Usuario findEntityByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    // Obtiene todos los usuarios sin passwords
    @Transactional(readOnly = true)
    @Override
    public List<UsuarioResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    // Busca un usuario por ID
    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto findById(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
        return mapper.toResponse(usuario);
    }

    // Busca un usuario por email
    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto findByEmail(String email) {
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email '" + email + "' no encontrado"));
        return mapper.toResponse(usuario);
    }

    // Crea un nuevo usuario con password encriptado
    @Override
    public UsuarioResponseDto create(UsuarioRequestDto dto) {
        // Validar que el email no exista
        if (repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Convertir DTO a entidad
        Usuario usuario = mapper.toEntity(dto);

        // Encriptar password antes de guardar
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Guardar y retornar
        Usuario saved = repo.save(usuario);
        return mapper.toResponse(saved);
    }

    // Actualiza usuario existente - password es opcional (si no se envía, se mantiene el actual)
    @Override
    public UsuarioResponseDto update(Long id, UsuarioUpdateDto dto) {
        // Buscar usuario existente
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));

        // Validar email único solo si cambió
        if (!dto.getEmail().equalsIgnoreCase(usuario.getEmail())
                && repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado por otro usuario");
        }

        // Actualizar campos básicos
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());

        // Solo actualizar password si se proporcionó uno nuevo (no null ni vacío)
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        // Si password es null o vacío, se mantiene el password actual

        // Guardar cambios y retornar
        Usuario updated = repo.save(usuario);
        return mapper.toResponse(updated);
    }

    // Elimina un usuario por ID
    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Usuario con ID " + id + " no encontrado");
        }
        repo.deleteById(id);
    }
}
