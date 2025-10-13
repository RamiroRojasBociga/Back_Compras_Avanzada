package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.ImpuestoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ImpuestoResponseDto;
import com.sistemacompras.sistemacompras_api.service.ImpuestoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "Impuestos")
@RestController
@RequestMapping("/api/Impuestos")
public class ImpuestoController {

    private final ImpuestoService service;

    public ImpuestoController(ImpuestoService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar Impuestos")
    @GetMapping
    public List<ImpuestoResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener Impuesto por ID")
    @GetMapping("/{id}")
    public ImpuestoResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear Impuesto")
    @PostMapping
    public ResponseEntity<ImpuestoResponseDto> create(@Valid @RequestBody ImpuestoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar Impuesto")
    @PutMapping("/{id}")
    public ImpuestoResponseDto update(@PathVariable Long id, @Valid @RequestBody ImpuestoRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar Impuesto")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

