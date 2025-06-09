package com.servicios.FoodExpress.model;

import com.servicios.FoodExpress.Category.ProductoCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigInteger;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Clase base de producto, incluye los atributos junto a un ID,
 * autogenerado.
 * 
 * @author Leandro
 */
@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "PRODUCTO")
public class Producto {

    // Id del producto, autogenerativo
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    // Nombre y descripción
    @NotBlank
    @Column(name = "producto_nombre", length = 50, nullable = false)
    private String nombre;
    @Column(name = "producto_description", length = 200)
    private String Description;

    // Precio
    @Positive
    @NotNull
    @Column(name = "producto_precio", precision = 19, scale = 0, nullable = false)
    private BigInteger precio;

    // Stock
    @PositiveOrZero
    @NotNull
    @Column(name = "producto_stock", nullable = false)
    private Integer stock;

    // Categoría del producto
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "producto_categoria", length = 20, nullable = false)
    private ProductoCategory categoria;

    // Fecha de creación del producto
    @CreationTimestamp
    @Column(name = "make_date", updatable = false)
    private LocalDate make_date;

    // Fecha de actualización del producto
    @UpdateTimestamp
    @Column(name = "update_date", updatable = true)
    private LocalDate update_date;

    // Manejo de versiones
    @Version
    private Long version;
}
