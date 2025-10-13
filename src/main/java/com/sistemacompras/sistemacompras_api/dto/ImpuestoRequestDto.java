// com.sistemacompras.sistemacompras_api.dto.CategoriaRequest
package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ImpuestoRequestDto {
    @NotBlank @Size(max = 120)
    private String nombre;

    @NotBlank @Size(max = 100)
    private Float porcentaje;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Float getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

}
