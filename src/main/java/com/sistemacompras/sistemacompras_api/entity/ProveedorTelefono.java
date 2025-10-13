package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedor_telefono")
public class ProveedorTelefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor_telefono")
    private Long idProveedorTelefono;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_telefono", nullable = false)
    private Telefono telefono;

    // Constructor vacío
    public ProveedorTelefono() {}

    // Constructor con parámetros
    public ProveedorTelefono(Proveedor proveedor, Telefono telefono) {
        this.proveedor = proveedor;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getIdProveedorTelefono() {
        return idProveedorTelefono;
    }

    public void setIdProveedorTelefono(Long idProveedorTelefono) {
        this.idProveedorTelefono = idProveedorTelefono;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }
}
