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

    @Transactional(readOnly = true)
    public List<DetalleCompraResponseDto> findAll() {
        // CORREGIDO: Usar JOIN FETCH
        List<DetalleCompra> detalles = repo.findAllWithRelations();
        return mapper.toResponseList(detalles);
    }

    @Transactional(readOnly = true)
    public DetalleCompraResponseDto findById(Long id) {
        //CORREGIDO: Usar JOIN FETCH
        DetalleCompra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public DetalleCompraResponseDto create(DetalleCompraRequestDto dto) {
        DetalleCompra entity = mapper.toEntity(dto);
        DetalleCompra saved = repo.save(entity);

        //CORREGIDO: Obtener con relaciones para la respuesta
        DetalleCompra savedWithRelations = repo.findByIdWithRelations(saved.getIdDetalleCompra())
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada después de crear"));

        return mapper.toResponse(savedWithRelations);
    }

    public DetalleCompraResponseDto update(Long id, DetalleCompraRequestDto dto) {
        // CORREGIDO: Usar JOIN FETCH
        DetalleCompra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));

        mapper.updateEntityFromRequest(dto, e);
        DetalleCompra updated = repo.save(e);

        //CORREGIDO: Obtener con relaciones para la respuesta
        DetalleCompra updatedWithRelations = repo.findByIdWithRelations(updated.getIdDetalleCompra())
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra no encontrada después de actualizar"));

        return mapper.toResponse(updatedWithRelations);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("DetalleCompra " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}