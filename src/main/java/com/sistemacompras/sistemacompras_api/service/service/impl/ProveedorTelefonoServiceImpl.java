package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.repository.ProveedorTelefonoRepository;
import com.sistemacompras.sistemacompras_api.service.ProveedorTelefonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorTelefonoServiceImpl implements ProveedorTelefonoService {

    @Autowired
    private ProveedorTelefonoRepository proveedorTelefonoRepository;

    @Override
    public List<ProveedorTelefono> listarTodos() {
        return proveedorTelefonoRepository.findAll();
    }

    @Override
    public Optional<ProveedorTelefono> buscarPorId(Long id) {
        return proveedorTelefonoRepository.findById(id);
    }

    @Override
    public ProveedorTelefono guardar(ProveedorTelefono proveedorTelefono) {
        return proveedorTelefonoRepository.save(proveedorTelefono);
    }

    @Override
    public void eliminar(Long id) {
        proveedorTelefonoRepository.deleteById(id);
    }

    @Override
    public List<ProveedorTelefono> buscarPorIdProveedor(Long idProveedor) {
        return proveedorTelefonoRepository.findByProveedor_IdProveedor(idProveedor);
    }

    @Override
    public List<ProveedorTelefono> buscarPorIdTelefono(Long idTelefono) {
        return proveedorTelefonoRepository.findByTelefono_Id(idTelefono);
    }
}
