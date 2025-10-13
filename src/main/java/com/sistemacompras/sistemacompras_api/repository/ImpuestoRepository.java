package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {
    Optional<Impuesto> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Impuesto> findByNombreContainingIgnoreCase(String nombre);
}
