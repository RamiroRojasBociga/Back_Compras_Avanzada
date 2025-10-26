package com.sistemacompras.sistemacompras_api.dto;

import com.sistemacompras.sistemacompras_api.enums.EstadoProducto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductoRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;

    @NotNull(message = "La marca es obligatoria")
    private Long idMarca;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Long idUnidadMedida;

    @NotNull(message = "El impuesto es obligatorio")
    private Long idImpuesto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidadUnidadesMedida;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El estado es obligatorio")
    private EstadoProducto estado;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public Long getIdMarca() { return idMarca; }
    public void setIdMarca(Long idMarca) { this.idMarca = idMarca; }

    public Long getIdUnidadMedida() { return idUnidadMedida; }
    public void setIdUnidadMedida(Long idUnidadMedida) { this.idUnidadMedida = idUnidadMedida; }

    public Long getIdImpuesto() { return idImpuesto; }
    public void setIdImpuesto(Long idImpuesto) { this.idImpuesto = idImpuesto; }

    public Integer getCantidadUnidadesMedida() { return cantidadUnidadesMedida; }
    public void setCantidadUnidadesMedida(Integer cantidadUnidadesMedida) { this.cantidadUnidadesMedida = cantidadUnidadesMedida; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public EstadoProducto getEstado() { return estado; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
}