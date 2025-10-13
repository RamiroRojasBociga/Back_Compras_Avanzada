package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DetalleCompraRequestDto {

    @NotNull
    private Long idCompra;

    @NotNull
    private Long idProducto;

    @NotNull
    @Positive
    private Integer cantidad;

    // Getters y setters

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
