package com.sistemacompras.sistemacompras_api.entity;

import com.sistemacompras.sistemacompras_api.enums.EstadoProveedor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "proveedores")
public class Proveedor {

    // Clave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Relación muchos-a-uno con Ciudad
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(length = 150)
    private String direccion;

    @Column(length = 100, nullable = false)
    private String email;

    // Estado manejado por ENUM
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProveedor estado;

    // Relación uno-a-muchos con teléfonos
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProveedorTelefono> telefonos;

    // Fecha de registro automática
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoProveedor.ACTIVO; // Valor por defecto
        }
    }

    // Getters y Setters
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

    public EstadoProveedor getEstado() {
        return estado;
    }

    public void setEstado(EstadoProveedor estado) {
        this.estado = estado;
    }

    public List<ProveedorTelefono> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<ProveedorTelefono> telefonos) {
        this.telefonos = telefonos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
