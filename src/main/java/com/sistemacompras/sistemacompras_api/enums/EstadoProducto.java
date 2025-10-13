// src/main/java/com/sistemacompras/sistemacompras_api/enums/EstadoProducto.java
package com.sistemacompras.sistemacompras_api.enums;

public enum EstadoProducto {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    AGOTADO("Agotado"),
    DESCONTINUADO("Descontinuado");

    private final String descripcion;

    EstadoProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}