package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.ProveedorRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProveedorResponseDto;
import com.sistemacompras.sistemacompras_api.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService service;

    public ProveedorController(ProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProveedorResponseDto> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProveedorResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProveedorResponseDto> create(@Valid @RequestBody ProveedorRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ProveedorResponseDto update(@PathVariable Long id, @Valid @RequestBody ProveedorRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}


