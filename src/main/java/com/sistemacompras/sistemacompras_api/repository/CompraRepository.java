package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // Buscar por estado, ignorando mayúsculas/minúsculas
    Optional<Compra> findByEstadoIgnoreCase(String estado);

    // Saber si existe alguna compra con un determinado estado
    boolean existsByEstadoIgnoreCase(String estado);

    // Buscar compras donde el estado contenga una cadena específica
    List<Compra> findByEstadoContainingIgnoreCase(String estado);

    // Buscar por fecha exacta
    List<Compra> findByFecha(LocalDate fecha);

}
