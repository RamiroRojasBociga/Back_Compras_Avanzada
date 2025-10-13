// com.sistemacompras.sistemacompras_api.dto.CategoriaResponse
package com.sistemacompras.sistemacompras_api.dto;

public class ImpuestoResponseDto {
    private Long id;
    private String nombre;
    private Float porcentaje;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Float getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Float porcentaje) { this.porcentaje = porcentaje; }

}
