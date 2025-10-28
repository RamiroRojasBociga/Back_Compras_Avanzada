package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorTelefonoRepository extends JpaRepository<ProveedorTelefono, Long> {

    // ✅ MÉTODOS EXISTENTES
    List<ProveedorTelefono> findByProveedor(Proveedor proveedor);
    List<ProveedorTelefono> findByTelefono(Telefono telefono);
    List<ProveedorTelefono> findByProveedor_IdProveedor(Long idProveedor);
    List<ProveedorTelefono> findByTelefono_Id(Long idTelefono);

    // ✅ NUEVO: Método que necesita el servicio
    @Modifying
    @Query("DELETE FROM ProveedorTelefono pt WHERE pt.proveedor.idProveedor = :idProveedor")
    void deleteByProveedorIdProveedor(@Param("idProveedor") Long idProveedor);

    // ✅ MÉTODOS ADICIONALES ÚTILES
    boolean existsByProveedorAndTelefono(Proveedor proveedor, Telefono telefono);

    Optional<ProveedorTelefono> findByProveedorIdProveedorAndTelefonoNumero(Long idProveedor, String numero);

    @Modifying
    @Query("DELETE FROM ProveedorTelefono pt WHERE pt.proveedor.idProveedor = :idProveedor AND pt.telefono.id = :idTelefono")
    void deleteByProveedorAndTelefono(@Param("idProveedor") Long idProveedor, @Param("idTelefono") Long idTelefono);
}