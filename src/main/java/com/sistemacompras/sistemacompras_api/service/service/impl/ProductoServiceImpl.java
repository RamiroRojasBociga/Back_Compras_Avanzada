// com.sistemacompras.sistemacompras_api.service.impl.ProductoServiceImpl
package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Producto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.ProductoMapper;
import com.sistemacompras.sistemacompras_api.repository.ProductoRepository;
import com.sistemacompras.sistemacompras_api.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final ProductoMapper mapper;

    public ProductoServiceImpl(ProductoRepository repo, ProductoMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDto> findAll() {
        return mapper.toResponseList(repo.findAll());
    }

    @Transactional(readOnly = true)
    public ProductoResponseDto findById(Long id) {
        Producto e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public ProductoResponseDto create(ProductoRequestDto dto) {
        if (repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del producto ya existe");
        }
        Producto saved = repo.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    public ProductoResponseDto update(Long id, ProductoRequestDto dto) {
        Producto e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto " + id + " no encontrada"));

        if (dto.getNombre() != null
                && !dto.getNombre().equalsIgnoreCase(e.getNombre())
                && repo.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new IllegalArgumentException("El nombre del producto ya existe");
        }

        mapper.updateEntityFromRequest(dto, e);
        return mapper.toResponse(repo.save(e));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Producto " + id + " no encontrada");
        }
        repo.deleteById(id);
    }

    // NUEVOS MÉTODOS CON ENUM
    @Transactional(readOnly = true)
    @Override
    public List<ProductoResponseDto> findByEstado(EstadoProducto estado) {
        List<Producto> productos = repo.findByEstado(estado);
        return mapper.toResponseList(productos);
    }

    @Override
    public void cambiarEstado(Long id, EstadoProducto nuevoEstado) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto " + id + " no encontrada"));
        producto.setEstado(nuevoEstado);
        repo.save(producto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoResponseDto> findProductosActivos() {
        return findByEstado(EstadoProducto.ACTIVO);
    }
}