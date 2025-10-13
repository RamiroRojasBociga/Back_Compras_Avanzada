package com.sistemacompras.sistemacompras_api.dto;

public class ProveedorTelefonoResponseDto {

    private Long idProveedorTelefono;
    private Long idProveedor;
    private String nombreProveedor;   // Si lo deseas mostrar
    private Long idTelefono;
    private String numeroTelefono;    // Si lo deseas mostrar

    // Getters y setters

    public Long getIdProveedorTelefono() {
        return idProveedorTelefono;
    }

    public void setIdProveedorTelefono(Long idProveedorTelefono) {
        this.idProveedorTelefono = idProveedorTelefono;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public Long getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Long idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }
}
