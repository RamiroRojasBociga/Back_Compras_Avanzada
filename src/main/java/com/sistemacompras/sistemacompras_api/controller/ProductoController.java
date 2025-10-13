package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.dto.ProductoRequestDto;
import com.sistemacompras.sistemacompras_api.dto.ProductoResponseDto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import com.sistemacompras.sistemacompras_api.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Opcional: si usas Springdoc OpenAPI
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;

// @Tag(name = "Productos")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // @Operation(summary = "Listar productos")
    @GetMapping
    public List<ProductoResponseDto> list() {
        return service.findAll();
    }

    // @Operation(summary = "Obtener producto por ID")
    @GetMapping("/{id}")
    public ProductoResponseDto get(@PathVariable Long id) {
        return service.findById(id);
    }

    // @Operation(summary = "Crear producto")
    @PostMapping
    public ResponseEntity<ProductoResponseDto> create(@Valid @RequestBody ProductoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    // @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ProductoResponseDto update(@PathVariable Long id, @Valid @RequestBody ProductoRequestDto dto) {
        return service.update(id, dto);
    }

    // @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ProductoResponseDto>> getProductosPorEstado(
            @PathVariable EstadoProducto estado) {
        List<ProductoResponseDto> productos = service.findByEstado(estado);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoResponseDto>> getProductosActivos() {
        List<ProductoResponseDto> productos = service.findProductosActivos();
        return ResponseEntity.ok(productos);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoProducto(
            @PathVariable Long id,
            @RequestBody Map<String, EstadoProducto> request) {

        EstadoProducto nuevoEstado = request.get("estado");
        service.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok().build();
    }
}

