package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    // Buscar todos los detalles por id de compra (FK)
    List<DetalleCompra> findByCompraIdCompra(Long idCompra);

    // Buscar todos los detalles por id de producto (FK)
    List<DetalleCompra> findByProductoIdProducto(Long idProducto);

    // Buscar todos los detalles por cantidad
    List<DetalleCompra> findByCantidad(Integer cantidad);
}
