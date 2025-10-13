package com.sistemacompras.sistemacompras_api.repository;


import com.sistemacompras.sistemacompras_api.entity.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {
    Optional<UnidadMedida> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<UnidadMedida> findByNombreContainingIgnoreCase(String nombre);
}
