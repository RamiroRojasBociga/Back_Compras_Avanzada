package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;


    public Categoria() {}

    public Categoria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

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

}

