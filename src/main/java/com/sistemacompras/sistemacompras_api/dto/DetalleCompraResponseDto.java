package com.sistemacompras.sistemacompras_api.dto;

public class DetalleCompraResponseDto {

    private Long idDetalleCompra;

    private Long idCompra;
    private String nombreUsuario;
    private Long idProducto;
    private String nombreProducto;

    private Integer cantidad;

    // Getters y setters

    public Long getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Long idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
