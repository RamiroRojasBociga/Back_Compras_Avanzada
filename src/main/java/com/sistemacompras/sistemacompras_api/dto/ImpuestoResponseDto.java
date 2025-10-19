// com.sistemacompras.sistemacompras_api.dto.CategoriaResponse
package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ImpuestoResponseDto {
    private Long id;
    private String nombre;

    @NotNull(message = "El porcentaje no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El porcentaje no puede ser menor que 0")
    @DecimalMax(value = "100.0", message = "El porcentaje no puede ser mayor que 100")
    private Float porcentaje;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Float getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Float porcentaje) { this.porcentaje = porcentaje; }

}
