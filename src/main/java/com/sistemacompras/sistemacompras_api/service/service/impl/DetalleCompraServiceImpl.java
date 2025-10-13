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
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public DetalleCompraResponseDto findById(Long id) {
        DetalleCompra e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public DetalleCompraResponseDto create(DetalleCompraRequestDto dto) {
        DetalleCompra saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public DetalleCompraResponseDto update(Long id, DetalleCompraRequestDto dto) {
        DetalleCompra e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DetalleCompra " + id + " no encontrada"));

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("DetalleCompra " + id + " no encontrada");
        }
        repo.deleteById(id);
    }
}
