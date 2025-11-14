package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    // Buscar todos los detalles por id de compra (FK)
    List<DetalleCompra> findByCompraIdCompra(Long idCompra);

    // Buscar todos los detalles por id de producto (FK)
    List<DetalleCompra> findByProductoIdProducto(Long idProducto);

    // Buscar todos los detalles por cantidad
    List<DetalleCompra> findByCantidad(Integer cantidad);

    // === MÉTODOS CON JOIN FETCH (NUEVOS) ===
    @Query("SELECT d FROM DetalleCompra d JOIN FETCH d.compra c JOIN FETCH c.usuario JOIN FETCH d.producto WHERE d.idDetalleCompra = :id")
    Optional<DetalleCompra> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT d FROM DetalleCompra d JOIN FETCH d.compra c JOIN FETCH c.usuario JOIN FETCH d.producto")
    List<DetalleCompra> findAllWithRelations();

    @Query("SELECT d FROM DetalleCompra d JOIN FETCH d.compra c JOIN FETCH c.usuario JOIN FETCH d.producto WHERE d.compra.idCompra = :idCompra")
    List<DetalleCompra> findByCompraWithRelations(@Param("idCompra") Long idCompra);



}