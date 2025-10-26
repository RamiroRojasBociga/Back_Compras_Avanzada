package com.sistemacompras.sistemacompras_api.enums;

public enum EstadoProveedor {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    BLOQUEADO("Bloqueado");

    private final String descripcion;

    EstadoProveedor(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
