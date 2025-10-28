package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {

    // ✅ MÉTODOS PRINCIPALES
    Optional<Telefono> findByNumero(String numero);
    boolean existsByNumero(String numero);

    // ✅ MÉTODOS ADICIONALES ÚTILES
    List<Telefono> findByNumeroContaining(String numero);

    @Query("SELECT t FROM Telefono t WHERE t.numero IN :numeros")
    List<Telefono> findByNumeros(@Param("numeros") List<String> numeros);

    // ✅ CORREGIDO: Usar "id" en lugar de "idTelefono"
    @Query("SELECT COUNT(t) > 0 FROM Telefono t WHERE t.numero = :numero AND t.id != :id")
    boolean existsByNumeroAndIdNot(@Param("numero") String numero, @Param("id") Long id);
}