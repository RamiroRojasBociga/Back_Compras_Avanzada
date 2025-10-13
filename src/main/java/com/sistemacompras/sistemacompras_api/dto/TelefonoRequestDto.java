// com.sistemacompras.sistemacompras_api.dto.CategoriaRequest
package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TelefonoRequestDto {
    @NotBlank @Size(max = 120)
    private String numero;
    @Size(max = 255)

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

}
