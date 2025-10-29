package com.sistemacompras.sistemacompras_api.dto;

import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CompraRequestDto {

    @Schema(description = "ID del usuario", example = "1")
    @NotNull
    private Long idUsuario;

    @Schema(description = "ID del proveedor", example = "1")
    @NotNull
    private Long idProveedor;

    @Schema(description = "Fecha de la compra", example = "2025-10-28")
    @NotNull
    private LocalDate fecha;

    @Schema(
            description = "Estado de la compra",
            example = "PENDIENTE",
            allowableValues = {"PENDIENTE", "PROCESADA", "ANULADA", "ENTREGADA", "FACTURADA"}
    )
    @NotNull
    private EstadoCompra estado;

    // ✅ ELIMINADO: numFactura - siempre se genera automáticamente

    // Constructores
    public CompraRequestDto() {}

    public CompraRequestDto(Long idUsuario, Long idProveedor, LocalDate fecha, EstadoCompra estado) {
        this.idUsuario = idUsuario;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y Setters (SIN numFactura)
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }
}