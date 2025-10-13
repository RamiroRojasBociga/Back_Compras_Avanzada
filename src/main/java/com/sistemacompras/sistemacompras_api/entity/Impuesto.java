package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Impuestos")
public class Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Impuesto")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name="porcentaje", nullable = false, length = 10)
    private Float porcentaje;


    public Impuesto() {}

    public Impuesto(Long id, String nombre, Float porcentaje) {
        this.id = id;
        this.nombre = nombre;
        this.porcentaje = porcentaje;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Float getPorcentaje() {return porcentaje;}
    public void setPorcentaje(Float porcentaje) {this.porcentaje = porcentaje;}

}

