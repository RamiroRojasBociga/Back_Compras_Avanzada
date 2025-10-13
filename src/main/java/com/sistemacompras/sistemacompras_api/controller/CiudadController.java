package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.CiudadRequestDto;
import com.sistemacompras.sistemacompras_api.dto.CiudadResponseDto;
import com.sistemacompras.sistemacompras_api.service.CiudadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "Ciudades")
@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService service;

    public CiudadController(CiudadService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar Ciudad")
    @GetMapping
    public List<CiudadResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener ciudad por ID")
    @GetMapping("/{id}")
    public CiudadResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear ciudad")
    @PostMapping
    public ResponseEntity<CiudadResponseDto> create(@Valid @RequestBody CiudadRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar ciudad")
    @PutMapping("/{id}")
    public CiudadResponseDto update(@PathVariable Long id, @Valid @RequestBody CiudadRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar ciudad")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

