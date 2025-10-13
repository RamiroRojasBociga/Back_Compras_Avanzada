package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.CategoriaRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CategoriaResponseDto;
import com.sistemacompras.sistemacompras_api.service.CategoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar categorías")
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public List<CategoriaResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public CategoriaResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear categoría")
    @PostMapping
    public ResponseEntity<CategoriaResponseDto> create(@Valid @RequestBody CategoriaRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar categoría")
    @PutMapping("/{id}")
    public CategoriaResponseDto update(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar categoría")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

