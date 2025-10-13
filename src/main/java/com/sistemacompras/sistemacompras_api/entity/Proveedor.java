package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    // Clave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    // Nombre del proveedor
    @Column(nullable = false)
    private String nombre;

    // Relación muchos-a-uno con ciudad
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    // Dirección del proveedor
    @Column(nullable = false)
    private String direccion;

    // Email del proveedor
    @Column(nullable = false)
    private String email;

    // Estado del proveedor (activo, inactivo, etc.)
    @Column(nullable = false)
    private String estado;

    // Getters y setters

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
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


