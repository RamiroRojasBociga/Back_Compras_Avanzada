package com.sistemacompras.sistemacompras_api.dto;

import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;

import java.time.LocalDate;

public class CompraResponseDto {

    private Long idCompra;
    private Long idUsuario;
    private String nombreUsuario;
    private Long idProveedor;
    private String nombreProveedor;
    private LocalDate fecha;
    private String numFactura;  // NUEVO CAMPO
    private EstadoCompra estado;

    // Constructores
    public CompraResponseDto() {}

    public CompraResponseDto(Long idCompra, Long idUsuario, String nombreUsuario,
                             Long idProveedor, String nombreProveedor, LocalDate fecha,
                             String numFactura, EstadoCompra estado) {
        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.fecha = fecha;
        this.numFactura = numFactura;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdCompra() { return idCompra; }
    public void setIdCompra(Long idCompra) { this.idCompra = idCompra; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getNumFactura() { return numFactura; }  // NUEVO
    public void setNumFactura(String numFactura) { this.numFactura = numFactura; }  // NUEVO

    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }
}