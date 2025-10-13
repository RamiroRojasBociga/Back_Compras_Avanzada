package com.sistemacompras.sistemacompras_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "telefonos")
public class Telefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_telefono")
    private Long id;

    @Column(name = "numero", nullable = false, length = 120)
    private String numero;


    public Telefono() {}

    public Telefono(Long id, String numero) {
        this.id = id;
        this.numero = numero;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}

