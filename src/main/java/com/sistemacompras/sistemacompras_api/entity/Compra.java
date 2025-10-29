package com.sistemacompras.sistemacompras_api.entity;

import com.sistemacompras.sistemacompras_api.enums.EstadoCompra;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // AGREGAR ESTE CAMPO - Número de factura (obligatorio)
    @Column(name = "num_factura", nullable = false)
    private String numFactura;

    // CAMBIO: De String a Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCompra estado;  // ANTES: private String estado;

    // Valor por defecto
    @PrePersist
    protected void onCreate() {
        if (this.estado == null) {
            this.estado = EstadoCompra.PENDIENTE;
        }
    }

    // Constructores
    public Compra() {}

    public Compra(Long idCompra, Usuario usuario, Proveedor proveedor, LocalDate fecha, String numFactura, EstadoCompra estado) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.numFactura = numFactura;
        this.estado = estado;
    }

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

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }
}