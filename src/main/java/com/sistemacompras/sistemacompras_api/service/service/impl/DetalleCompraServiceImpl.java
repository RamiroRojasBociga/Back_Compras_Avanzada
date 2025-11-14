// com.sistemacompras.sistemacompras_api.service.service.impl.DetalleCompraServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.DetalleCompraMapper;
import com.sistemacompras.sistemacompras_api.repository.DetalleCompraRepository;
import com.sistemacompras.sistemacompras_api.service.DetalleCompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DetalleCompraServiceImpl implements DetalleCompraService {

    private final DetalleCompraRepository repo;
    private final DetalleCompraMapper mapper;

    public DetalleCompraServiceImpl(DetalleCompraRepository repo, DetalleCompraMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleCompraResponseDto> findAll() {
        // Usamos el método con JOIN FETCH para traer relaciones
        List<DetalleCompra> detalles = repo.findAllWithRelations();
        return mapper.toResponseList(detalles);
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleCompraResponseDto findById(Long id) {
        // Usamos el método con JOIN FETCH para traer relaciones
        DetalleCompra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    @Override
    public DetalleCompraResponseDto create(DetalleCompraRequestDto dto) {
        // Convertimos el DTO a entidad
        DetalleCompra entity = mapper.toEntity(dto);

        // Guardamos la entidad
        DetalleCompra saved = repo.save(entity);

        // Volvemos a buscarla con las relaciones cargadas
        DetalleCompra savedWithRelations = repo.findByIdWithRelations(saved.getIdDetalleCompra())
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada después de crear"));

        // Devolvemos el DTO de respuesta
        return mapper.toResponse(savedWithRelations);
    }

    @Override
    public DetalleCompraResponseDto update(Long id, DetalleCompraRequestDto dto) {
        // Buscamos el detalle existente con relaciones
        DetalleCompra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));

        // Actualizamos los campos simples desde el DTO (cantidad, etc.)
        mapper.updateEntityFromRequest(dto, e);

        // Guardamos los cambios
        DetalleCompra updated = repo.save(e);

        // Volvemos a buscarlo con relaciones
        DetalleCompra updatedWithRelations = repo.findByIdWithRelations(updated.getIdDetalleCompra())
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada después de actualizar"));

        // Devolvemos el DTO de respuesta
        return mapper.toResponse(updatedWithRelations);
    }

    @Override
    public void delete(Long id) {
        // Validamos que exista antes de eliminar
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("DetalleCompra " + id + " no encontrada");
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleCompraResponseDto> findByCompra(Long idCompra) {
        // NUEVO: obtenemos todos los detalles de una compra, con relaciones
        List<DetalleCompra> detalles = repo.findByCompraWithRelations(idCompra);
        return mapper.toResponseList(detalles);
    }
}
