package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
}
