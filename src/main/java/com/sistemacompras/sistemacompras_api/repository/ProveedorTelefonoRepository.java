package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.ProveedorTelefono;
import com.sistemacompras.sistemacompras_api.entity.Proveedor;
import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProveedorTelefonoRepository extends JpaRepository<ProveedorTelefono, Long> {

    List<ProveedorTelefono> findByProveedor(Proveedor proveedor);

    List<ProveedorTelefono> findByTelefono(Telefono telefono);

    List<ProveedorTelefono> findByProveedor_IdProveedor(Long idProveedor);

    List<ProveedorTelefono> findByTelefono_Id(Long idTelefono);

}
