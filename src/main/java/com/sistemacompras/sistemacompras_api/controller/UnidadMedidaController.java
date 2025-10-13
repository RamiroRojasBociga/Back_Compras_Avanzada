package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UnidadMedidaResponseDto;
import com.sistemacompras.sistemacompras_api.service.UnidadMedidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "UnidadMedida")
@RestController
@RequestMapping("/api/unidades_medida")
public class UnidadMedidaController {

    private final UnidadMedidaService service;

    public UnidadMedidaController(UnidadMedidaService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar Unidades de Medida")
    @GetMapping
    public List<UnidadMedidaResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener Unidades Medida por ID")
    @GetMapping("/{id}")
    public UnidadMedidaResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear Unidades de Medida")
    @PostMapping
    public ResponseEntity<UnidadMedidaResponseDto> create(@Valid @RequestBody UnidadMedidaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar unidades de medidaa")
    @PutMapping("/{id}")
    public UnidadMedidaResponseDto update(@PathVariable Long id, @Valid @RequestBody UnidadMedidaRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

