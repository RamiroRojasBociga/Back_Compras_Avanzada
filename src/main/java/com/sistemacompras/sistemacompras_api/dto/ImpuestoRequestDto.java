package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ImpuestoRequestDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    private String nombre;

    // --- ESTA ES LA CORRECCIÓN ---
    // Se reemplaza @Size por anotaciones numéricas.
    @NotNull(message = "El porcentaje no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje no puede ser menor que 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "El porcentaje no puede ser mayor que 100")
    private Float porcentaje;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }
}
