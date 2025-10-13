package com.sistemacompras.sistemacompras_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProveedorRequestDto {

    @NotBlank
    @Size(max = 120)
    private String nombre;

    @NotNull
    private Long idCiudad;

    @NotBlank
    @Size(max = 150)
    private String direccion;

    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String estado;

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

