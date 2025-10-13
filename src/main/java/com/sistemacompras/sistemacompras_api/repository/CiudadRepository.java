package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Optional<Ciudad> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Ciudad> findByNombreContainingIgnoreCase(String nombre);
}
