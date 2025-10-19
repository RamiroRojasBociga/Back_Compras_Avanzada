package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TelefonoRequestDto {

    @NotBlank(message = "El número no puede estar vacío")
    @Size(max = 50, message = "El número no debe exceder los 50 caracteres")
    private String numero;

    // NO se necesita idProveedor aquí

    // Getters y Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
