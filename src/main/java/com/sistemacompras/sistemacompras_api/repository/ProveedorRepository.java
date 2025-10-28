package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    // TelefonoRepository
    public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
        Optional<Telefono> findByNumero(String numero);
    }

    // ProveedorTelefonoRepository
    public interface ProveedorTelefonoRepository extends JpaRepository<ProveedorTelefono, Long> {
        List<ProveedorTelefono> findByProveedorIdProveedor(Long idProveedor);
        void deleteByProveedorIdProveedor(Long idProveedor);
    }
    // === MÉTODOS CON JOIN FETCH (AGREGA ESTOS) ===
    @Query("SELECT p FROM Proveedor p JOIN FETCH p.ciudad LEFT JOIN FETCH p.telefonos t LEFT JOIN FETCH t.telefono WHERE p.idProveedor = :id")
    Optional<Proveedor> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Proveedor p JOIN FETCH p.ciudad LEFT JOIN FETCH p.telefonos t LEFT JOIN FETCH t.telefono")
    List<Proveedor> findAllWithRelations();

}
