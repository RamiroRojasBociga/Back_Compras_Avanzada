package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.CompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CompraResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import com.sistemacompras.sistemacompras_api.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService service;

    public CompraController(CompraService service) {
        this.service = service;
    }

    // ✅ ENDPOINTS EXISTENTES (funcionan igual)
    @GetMapping
    public List<CompraResponseDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CompraResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CompraResponseDto> create(@Valid @RequestBody CompraRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public CompraResponseDto update(@PathVariable Long id, @Valid @RequestBody CompraRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ✅ NUEVOS ENDPOINTS PARA ESTADOS
    @GetMapping("/estado/{estado}")
    public List<CompraResponseDto> getByEstado(@PathVariable EstadoCompra estado) {
        return service.findByEstado(estado);
    }

    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable Long id, @RequestBody EstadoCompra nuevoEstado) {
        service.cambiarEstado(id, nuevoEstado);
    }

    @GetMapping("/pendientes")
    public List<CompraResponseDto> getPendientes() {
        return service.findComprasPendientes();
    }

    @GetMapping("/procesadas")
    public List<CompraResponseDto> getProcesadas() {
        return service.findComprasProcesadas();
    }
}