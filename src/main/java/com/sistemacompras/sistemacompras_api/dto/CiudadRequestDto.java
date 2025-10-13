// com.sistemacompras.sistemacompras_api.dto.CategoriaRequest
package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CiudadRequestDto {
    @NotBlank @Size(max = 120)
    private String nombre;
    @Size(max = 255)

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

}
