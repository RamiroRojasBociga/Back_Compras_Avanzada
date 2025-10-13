package com.sistemacompras.sistemacompras_api.repository;


import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
    Optional<Telefono> findByNumeroIgnoreCase(String numero);
    boolean existsByNumeroIgnoreCase(String nombre);
    List<Telefono> findByNumeroContainingIgnoreCase(String numero);
}
