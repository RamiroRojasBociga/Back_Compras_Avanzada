package com.sistemacompras.sistemacompras_api.dto;

import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;

import java.math.BigDecimal;

public class ProductoResponseDto {

    private Long idProducto;
    private String nombre;

    // Información de las entidades relacionadas
    private Long idCategoria;
    private String nombreCategoria;

    private Long idMarca;
    private String nombreMarca;

    private Long idUnidadMedida;
    private String nombreUnidadMedida;

    private Float cantidadUnidadesMedida;

    private Long idImpuesto;
    private String nombreImpuesto;
    private Float porcentajeImpuesto;

    private BigDecimal precio;
    private Integer stock;

    private EstadoProducto estado;
    private String descripcion;

    // Getters y setters


    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Long getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Long idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public Long getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Long idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }

    public void setNombreUnidadMedida(String nombreUnidadMedida) {
        this.nombreUnidadMedida = nombreUnidadMedida;
    }

    public Float getCantidadUnidadesMedida() {
        return cantidadUnidadesMedida;
    }

    public void setCantidadUnidadesMedida(Float cantidadUnidadesMedida) {
        this.cantidadUnidadesMedida = cantidadUnidadesMedida;
    }

    public Long getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Long idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getNombreImpuesto() {
        return nombreImpuesto;
    }

    public void setNombreImpuesto(String nombreImpuesto) {
        this.nombreImpuesto = nombreImpuesto;
    }

    public Float getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(Float porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
