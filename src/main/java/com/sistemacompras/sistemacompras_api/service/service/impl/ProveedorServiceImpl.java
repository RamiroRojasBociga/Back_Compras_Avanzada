package com.sistemacompras.sistemacompras_api.service.service.impl; // Corrige el paquete

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ProveedorMapper;
import com.sistemacompras.sistemacompras_api.repository.ProveedorRepository;
import com.sistemacompras.sistemacompras_api.repository.ProveedorTelefonoRepository;
import com.sistemacompras.sistemacompras_api.repository.TelefonoRepository;
import com.sistemacompras.sistemacompras_api.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repo;
    private final ProveedorMapper mapper;
    private final TelefonoRepository telefonoRepo;
    private final ProveedorTelefonoRepository proveedorTelefonoRepo;

    public ProveedorServiceImpl(ProveedorRepository repo, ProveedorMapper mapper,
                                TelefonoRepository telefonoRepo, ProveedorTelefonoRepository proveedorTelefonoRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.telefonoRepo = telefonoRepo;
        this.proveedorTelefonoRepo = proveedorTelefonoRepo;
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDto> findAll() {
        List<Proveedor> proveedores = repo.findAllWithRelations();
        return mapper.toResponseList(proveedores);
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDto findById(Long id) {
        Proveedor proveedor = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor " + id + " no encontrado"));
        return mapper.toResponse(proveedor);
    }

    public ProveedorResponseDto create(ProveedorRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del proveedor ya existe");
        }

        Proveedor entity = mapper.toEntity(dto);
        Proveedor saved = repo.save(entity);

        // Manejar teléfonos
        if (dto.getTelefonos() != null && !dto.getTelefonos().isEmpty()) {
            guardarTelefonosProveedor(saved, dto.getTelefonos());
        }

        return mapper.toResponse(repo.findByIdWithRelations(saved.getIdProveedor())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado después de crear")));
    }

    public ProveedorResponseDto update(Long id, ProveedorRequestDto dto) {
        Proveedor proveedor = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor " + id + " no encontrado"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(proveedor.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del proveedor ya existe");
        }

        mapper.updateEntityFromRequest(dto, proveedor);
        Proveedor updated = repo.save(proveedor);

        // Actualizar teléfonos si se proporcionan
        if (dto.getTelefonos() != null) {
            // Eliminar teléfonos existentes
            proveedorTelefonoRepo.deleteByProveedorIdProveedor(id);

            // Agregar nuevos teléfonos
            if (!dto.getTelefonos().isEmpty()) {
                guardarTelefonosProveedor(updated, dto.getTelefonos());
            }
        }

        return mapper.toResponse(repo.findByIdWithRelations(updated.getIdProveedor())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado después de actualizar")));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Proveedor " + id + " no encontrado");
        }

        // Eliminar relaciones de teléfonos primero
        proveedorTelefonoRepo.deleteByProveedorIdProveedor(id);

        // Eliminar el proveedor
        repo.deleteById(id);
    }

    private void guardarTelefonosProveedor(Proveedor proveedor, List<String> numeros) {
        for (String numero : numeros) {
            // Buscar si el teléfono ya existe
            Telefono telefono = telefonoRepo.findByNumero(numero)
                    .orElseGet(() -> {
                        Telefono nuevoTelefono = new Telefono();
                        nuevoTelefono.setNumero(numero);
                        return telefonoRepo.save(nuevoTelefono);
                    });

            // Crear relación
            ProveedorTelefono proveedorTelefono = new ProveedorTelefono();
            proveedorTelefono.setProveedor(proveedor);
            proveedorTelefono.setTelefono(telefono);
            proveedorTelefonoRepo.save(proveedorTelefono);
        }
    }

    // Métodos adicionales útiles
    @Transactional(readOnly = true)
    public List<ProveedorResponseDto> findByNombreContaining(String nombre) {
        List<Proveedor> proveedores = repo.findByNombreContainingIgnoreCase(nombre);
        return mapper.toResponseList(proveedores);
    }

    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombreIgnoreCase(nombre);
    }
}