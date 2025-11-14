package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.service.DetalleCompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "DetalleCompras")
@RestController
@RequestMapping("/api/DetalleCompras")
public class DetalleCompraController {

    private final DetalleCompraService service;

    public DetalleCompraController(DetalleCompraService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar Detalles")
    @GetMapping
    public List<DetalleCompraResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener Detalle por ID")
    @GetMapping("/{id}")
    public DetalleCompraResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear Detalle")
    @PostMapping
    public ResponseEntity<DetalleCompraResponseDto> create(@Valid @RequestBody DetalleCompraRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar Detalle")
    @PutMapping("/{id}")
    public DetalleCompraResponseDto update(@PathVariable Long id, @Valid @RequestBody DetalleCompraRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar Detalle")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}