package com.sistemacompras.sistemacompras_api.dto;

import com.sistemacompras.sistemacompras_api.enums.EstadoProveedor;
import java.time.LocalDateTime;
import java.util.List;

public class ProveedorResponseDto {

    private Long idProveedor;
    private String nombre;
    private Long idCiudad;
    private String nombreCiudad;
    private String direccion;
    private String email;
    private EstadoProveedor estado;
    private LocalDateTime fechaRegistro;
    private List<String> telefonos; // Teléfonos en la respuesta

    // Getters y Setters
    public Long getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Long idProveedor) { this.idProveedor = idProveedor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getIdCiudad() { return idCiudad; }
    public void setIdCiudad(Long idCiudad) { this.idCiudad = idCiudad; }

    public String getNombreCiudad() { return nombreCiudad; }
    public void setNombreCiudad(String nombreCiudad) { this.nombreCiudad = nombreCiudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public EstadoProveedor getEstado() { return estado; }
    public void setEstado(EstadoProveedor estado) { this.estado = estado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public List<String> getTelefonos() { return telefonos; }
    public void setTelefonos(List<String> telefonos) { this.telefonos = telefonos; }
}