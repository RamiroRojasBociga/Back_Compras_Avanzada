package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Producto;
import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // NUEVOS MÉTODOS CON ENUM
    List<Producto> findByEstado(EstadoProducto estado);
    Long countByEstado(EstadoProducto estado);

    // Opcional: Buscar por múltiples estados
    List<Producto> findByEstadoIn(List<EstadoProducto> estados);
}
