package com.sistemacompras.sistemacompras_api.dto;

public class EstadosProductosResponseDto {
    private String valor;   // "ACTIVO", "INACTIVO"
    private String label;   // "Activo", "Inactivo"

    // Constructor vacío (necesario para Spring)
    public EstadosProductosResponseDto() {}

    // Constructor con parámetros
    public EstadosProductosResponseDto(String valor, String label) {
        this.valor = valor;
        this.label = label;
    }

    // Getters y Setters
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}

