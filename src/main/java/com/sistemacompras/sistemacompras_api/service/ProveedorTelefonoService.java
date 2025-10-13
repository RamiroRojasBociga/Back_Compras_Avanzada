package com.sistemacompras.sistemacompras_api.service;

import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;

import java.util.List;
import java.util.Optional;

public interface ProveedorTelefonoService {

    // Obtener todos los registros
    List<ProveedorTelefono> listarTodos();

    // Buscar por ID
    Optional<ProveedorTelefono> buscarPorId(Long id);

    // Guardar o actualizar
    ProveedorTelefono guardar(ProveedorTelefono proveedorTelefono);

    // Eliminar por ID
    void eliminar(Long id);

    // Buscar por id del proveedor
    List<ProveedorTelefono> buscarPorIdProveedor(Long idProveedor);

    // Buscar por id del teléfono
    List<ProveedorTelefono> buscarPorIdTelefono(Long idTelefono);
}

