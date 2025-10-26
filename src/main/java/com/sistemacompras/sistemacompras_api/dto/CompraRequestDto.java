package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CompraRequestDto {

    @NotNull
    private Long idUsuario;

    @NotNull
    private Long idProveedor;

    @NotNull
    private LocalDate fecha;

    private String numFactura;  // QUITADO @NotBlank - ahora puede ser null

    @NotBlank
    private String estado;

    // Constructores
    public CompraRequestDto() {}

    public CompraRequestDto(Long idUsuario, Long idProveedor, LocalDate fecha, String numFactura, String estado) {
        this.idUsuario = idUsuario;
        this.idProveedor = idProveedor;
        this.fecha = fecha;
        this.numFactura = numFactura;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNumFactura() { return numFactura; }
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}