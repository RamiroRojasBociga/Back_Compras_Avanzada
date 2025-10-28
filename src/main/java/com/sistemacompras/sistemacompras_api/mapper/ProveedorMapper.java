package com.sistemacompras.sistemacompras_api.mapper;

import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // === MÉTODOS CON JOIN FETCH (NUEVOS) ===
    @Query("SELECT p FROM Proveedor p JOIN FETCH p.ciudad LEFT JOIN FETCH p.telefonos t LEFT JOIN FETCH t.telefono WHERE p.idProveedor = :id")
    Optional<Proveedor> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Proveedor p JOIN FETCH p.ciudad LEFT JOIN FETCH p.telefonos t LEFT JOIN FETCH t.telefono")
    List<Proveedor> findAllWithRelations();

    // === MÉTODOS EXISTENTES ===
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
}