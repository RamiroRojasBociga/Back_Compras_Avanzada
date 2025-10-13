package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "compras")
public class Compra {

    // Clave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    // Relación muchos-a-uno con usuario (quien registra la compra)
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Relación muchos-a-uno con proveedor (a quién se realiza la compra)
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    // Fecha de la compra
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Estado de la compra (ej: registrada, pagada, anulada)
    @Column(name = "estado", nullable = false)
    private String estado;

    // Getters y setters

    public Long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
