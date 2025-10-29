package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Compra;
import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.CompraMapper;
import com.sistemacompras.sistemacompras_api.repository.CompraRepository;
import com.sistemacompras.sistemacompras_api.service.CompraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        List<Compra> compras = repo.findAllWithRelations();
        return mapper.toResponseList(compras); // ✅ Ahora este método existe
    }

    @Transactional(readOnly = true)
    public CompraResponseDto findById(Long id) {
        Compra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra " + id + " no encontrada"));
        return mapper.toResponse(e);
    }

    public CompraResponseDto create(CompraRequestDto dto) {
        // Validar y asignar estado por defecto si es null
        if (dto.getEstado() == null) {
            dto.setEstado(EstadoCompra.PENDIENTE);
        }

        // ✅ SIEMPRE generar número de factura automáticamente
        String numeroFactura = generarNumeroFactura();

        Compra entity = mapper.toEntity(dto);
        entity.setNumFactura(numeroFactura); // ✅ Asignar siempre

        Compra saved = repo.save(entity);

        // Para la respuesta, obtener la entidad con relaciones cargadas
        Compra savedWithRelations = repo.findByIdWithRelations(saved.getIdCompra())
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada después de crear"));

        return mapper.toResponse(savedWithRelations);
    }

    public CompraResponseDto update(Long id, CompraRequestDto dto) {
        Compra e = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra " + id + " no encontrada"));

        mapper.updateEntityFromRequest(dto, e);
        Compra updated = repo.save(e);

        // Obtener con relaciones para la respuesta
        Compra updatedWithRelations = repo.findByIdWithRelations(updated.getIdCompra())
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada después de actualizar"));

        return mapper.toResponse(updatedWithRelations);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Compra " + id + " no encontrada");
        }
        repo.deleteById(id);
    }

    // ✅ NUEVOS MÉTODOS PARA MANEJAR ESTADOS
    @Transactional(readOnly = true)
    public List<CompraResponseDto> findByEstado(EstadoCompra estado) {
        List<Compra> compras = repo.findByEstado(estado);
        return mapper.toResponseList(compras); // ✅ Usar el nuevo método
    }

    public void cambiarEstado(Long id, EstadoCompra nuevoEstado) {
        Compra compra = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra " + id + " no encontrada"));
        compra.setEstado(nuevoEstado);
        repo.save(compra);
    }

    @Transactional(readOnly = true)
    public List<CompraResponseDto> findComprasPendientes() {
        return findByEstado(EstadoCompra.PENDIENTE);
    }

    @Transactional(readOnly = true)
    public List<CompraResponseDto> findComprasProcesadas() {
        return findByEstado(EstadoCompra.PROCESADA);
    }

    // Método para generar número de factura automático
    private String generarNumeroFactura() {
        long totalCompras = repo.count();
        String año = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        return "FACT-" + año + "-" + String.format("%04d", totalCompras + 1);
    }
}