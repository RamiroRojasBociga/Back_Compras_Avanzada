package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotNull;

public class ProveedorTelefonoRequestDto {

    @NotNull
    private Long idProveedor;

    @NotNull
    private Long idTelefono;

    // Getters y setters

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Long idTelefono) {
        this.idTelefono = idTelefono;
    }
}
