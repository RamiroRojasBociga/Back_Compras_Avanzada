package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Producto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // === MÉTODOS CON JOIN FETCH (NUEVOS) ===
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria JOIN FETCH p.marca JOIN FETCH p.unidadMedida JOIN FETCH p.impuesto WHERE p.idProducto = :id")
    Optional<Producto> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria JOIN FETCH p.marca JOIN FETCH p.unidadMedida JOIN FETCH p.impuesto")
    List<Producto> findAllWithRelations();

    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria JOIN FETCH p.marca JOIN FETCH p.unidadMedida JOIN FETCH p.impuesto WHERE p.estado = :estado")
    List<Producto> findByEstadoWithRelations(@Param("estado") EstadoProducto estado);

    // === MÉTODOS EXISTENTES ===
    boolean existsByNombreIgnoreCase(String nombre);
    List<Producto> findByEstado(EstadoProducto estado);
}