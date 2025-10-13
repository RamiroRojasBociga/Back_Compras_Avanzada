package com.sistemacompras.sistemacompras_api.controller;

import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.service.ProveedorTelefonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedor-telefonos")

public class ProveedorTelefonoController {

    @Autowired
    private ProveedorTelefonoService proveedorTelefonoService;

    // Obtener todos
    @GetMapping
    public ResponseEntity<List<ProveedorTelefono>> listarTodos() {
        List<ProveedorTelefono> lista = proveedorTelefonoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorTelefono> buscarPorId(@PathVariable Long id) {
        return proveedorTelefonoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo registro
    @PostMapping
    public ResponseEntity<ProveedorTelefono> crear(@RequestBody ProveedorTelefono proveedorTelefono) {
        ProveedorTelefono nuevo = proveedorTelefonoService.guardar(proveedorTelefono);
        return ResponseEntity.ok(nuevo);
    }

    // Actualizar registro existente
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorTelefono> actualizar(@PathVariable Long id,
                                                        @RequestBody ProveedorTelefono proveedorTelefono) {
        return proveedorTelefonoService.buscarPorId(id)
                .map(existente -> {
                    proveedorTelefono.setIdProveedorTelefono(id);
                    ProveedorTelefono actualizado = proveedorTelefonoService.guardar(proveedorTelefono);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (proveedorTelefonoService.buscarPorId(id).isPresent()) {
            proveedorTelefonoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por idProveedor
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<ProveedorTelefono>> buscarPorIdProveedor(@PathVariable Long idProveedor) {
        List<ProveedorTelefono> lista = proveedorTelefonoService.buscarPorIdProveedor(idProveedor);
        return ResponseEntity.ok(lista);
    }

    // Buscar por idTelefono
    @GetMapping("/telefono/{idTelefono}")
    public ResponseEntity<List<ProveedorTelefono>> buscarPorIdTelefono(@PathVariable Long idTelefono) {
        List<ProveedorTelefono> lista = proveedorTelefonoService.buscarPorIdTelefono(idTelefono);
        return ResponseEntity.ok(lista);
    }
}
