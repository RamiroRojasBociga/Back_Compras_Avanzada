package com.sistemacompras.sistemacompras_api.entity;

import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import jakarta.persistence.*;
import java.math.BigDecimal;

// La clase representa la tabla "productos" en la base de datos
@Entity
@Table(name = "productos")
public class Producto {

    // Clave primaria autoincremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    // Nombre del producto, obligatorio
    @Column(nullable = false)
    private String nombre;

    // Relación muchos-a-uno: muchos productos pueden ser de una categoría
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    // Relación muchos-a-uno: muchos productos pueden pertenecer a una marca
    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    // Relación muchos-a-uno: muchos productos pueden tener una unidad de medida
    @ManyToOne
    @JoinColumn(name = "id_unidad_medida", nullable = false)
    private UnidadMedida unidadMedida;

    // Cantidad (decimal) de unidades según la unidad de medida seleccionada
    @Column(name = "cantidad_unidades_medida", nullable = false)
    private Float cantidadUnidadesMedida;

    // Relación muchos-a-uno: muchos productos pueden tener un impuesto
    @ManyToOne
    @JoinColumn(name = "id_impuesto", nullable = false)
    private Impuesto impuesto;

    // Precio del producto
    @Column(nullable = false)
    private BigDecimal precio;

    // Cantidad disponible en stock
    @Column(nullable = false)
    private Integer stock;

    // Estado del producto (activo, inactivo, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoProducto estado;

    // Getters y setters


    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Float getCantidadUnidadesMedida() {
        return cantidadUnidadesMedida;
    }

    public void setCantidadUnidadesMedida(Float cantidadUnidadesMedida) {
        this.cantidadUnidadesMedida = cantidadUnidadesMedida;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
}


