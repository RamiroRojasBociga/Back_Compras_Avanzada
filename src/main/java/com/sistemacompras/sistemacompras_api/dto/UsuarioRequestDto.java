package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;

public class UsuarioRequestDto {

    @NotBlank
    @Size(max = 120)
    private String nombre;

    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telefono;

    @NotBlank
    @Size(max = 100)
    private String password;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

