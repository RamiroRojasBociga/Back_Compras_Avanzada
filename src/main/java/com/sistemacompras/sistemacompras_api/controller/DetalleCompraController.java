// com.sistemacompras.sistemacompras_api.controller.DetalleCompraController
package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.DetalleCompraRequestDto;
import com.sistemacompras.sistemacompras_api.dto.DetalleCompraResponseDto;
import com.sistemacompras.sistemacompras_api.service.DetalleCompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController para exponer API REST
@RestController
@RequestMapping("/api/DetalleCompras")
public class DetalleCompraController {

    private final DetalleCompraService service;

    public DetalleCompraController(DetalleCompraService service) {
        this.service = service;
    }

    // Listar todos los detalles
    @GetMapping
    public List<DetalleCompraResponseDto> list() {
        return service.findAll();
    }

    // Obtener un detalle por ID
    @GetMapping("/{id}")
    public DetalleCompraResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // ✅ NUEVO: listar todos los detalles de una compra específica
    // GET /api/DetalleCompras/compra/{idCompra}
    @GetMapping("/compra/{idCompra}")
    public List<DetalleCompraResponseDto> listByCompra(@PathVariable Long idCompra) {
        return service.findByCompra(idCompra);
    }

    // Crear un detalle
    @PostMapping
    public ResponseEntity<DetalleCompraResponseDto> create(@Valid @RequestBody DetalleCompraRequestDto dto) {
        DetalleCompraResponseDto creado = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Actualizar un detalle
    @PutMapping("/{id}")
    public DetalleCompraResponseDto update(@PathVariable Long id, @Valid @RequestBody DetalleCompraRequestDto dto) {
        return service.update(id, dto);
    }

    // Eliminar un detalle
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
