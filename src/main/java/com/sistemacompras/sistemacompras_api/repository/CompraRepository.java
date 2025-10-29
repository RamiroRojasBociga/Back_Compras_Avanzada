package com.sistemacompras.sistemacompras_api.repository;

import com.sistemacompras.sistemacompras_api.entity.Compra;
import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    // === MÉTODOS PERSONALIZADOS CON JOIN FETCH ===

    @Query("SELECT c FROM Compra c JOIN FETCH c.usuario JOIN FETCH c.proveedor WHERE c.idCompra = :id")
    Optional<Compra> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT c FROM Compra c JOIN FETCH c.usuario JOIN FETCH c.proveedor")
    List<Compra> findAllWithRelations();

    // === MÉTODOS CORREGIDOS PARA ENUM ===

    // ✅ ÚNICO método para buscar por estado
    List<Compra> findByEstado(EstadoCompra estado);

    // ✅ Método para verificar existencia por estado
    boolean existsByEstado(EstadoCompra estado);

    // ✅ ELIMINADO: El Optional ya no es necesario porque findByEstado devuelve List

    // ✅ OPCIONAL: Método para buscar una compra específica por estado (si lo necesitas)
    Optional<Compra> findFirstByEstado(EstadoCompra estado);

    // ✅ OPCIONAL: Método personalizado si necesitas búsqueda por texto
    @Query("SELECT c FROM Compra c WHERE UPPER(CAST(c.estado AS string)) LIKE UPPER(CONCAT('%', :texto, '%'))")
    List<Compra> findByEstadoTexto(@Param("texto") String texto);

    // === MÉTODOS QUE NO CAMBIAN ===

    // Buscar por fecha exacta
    List<Compra> findByFecha(LocalDate fecha);
}