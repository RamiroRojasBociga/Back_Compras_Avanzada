package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
