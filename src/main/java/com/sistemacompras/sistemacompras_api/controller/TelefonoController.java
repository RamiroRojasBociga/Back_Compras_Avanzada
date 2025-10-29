package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.TelefonoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.TelefonoResponseDto;
import com.sistemacompras.sistemacompras_api.service.TelefonoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "Telefonos")
//@RestController
@RequestMapping("/api/Telefonos")
public class TelefonoController {

    private final TelefonoService service;

    public TelefonoController(TelefonoService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar Telefonos")
    @GetMapping
    public List<TelefonoResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener Telefono por ID")
    @GetMapping("/{id}")
    public TelefonoResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear Telefono")
    @PostMapping
    public ResponseEntity<TelefonoResponseDto> create(@Valid @RequestBody TelefonoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar Telefono")
    @PutMapping("/{id}")
    public TelefonoResponseDto update(@PathVariable Long id, @Valid @RequestBody TelefonoRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar Telefono")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

