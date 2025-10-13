package com.sistemacompras.sistemacompras_api.controller;


import com.sistemacompras.sistemacompras_api.dto.MarcaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.MarcaResponseDto;
import com.sistemacompras.sistemacompras_api.service.MarcaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "Categorias")
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar marcas")
    @GetMapping
    public List<MarcaResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener Marca por ID")
    @GetMapping("/{id}")
    public MarcaResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear Marca")
    @PostMapping
    public ResponseEntity<MarcaResponseDto> create(@Valid @RequestBody MarcaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar Marca")
    @PutMapping("/{id}")
    public MarcaResponseDto update(@PathVariable Long id, @Valid @RequestBody MarcaRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar Marca")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

