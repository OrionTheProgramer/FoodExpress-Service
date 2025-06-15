package com.servicios.FoodExpress.model;

import com.servicios.FoodExpress.Category.ProductoCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data @Builder
public class PlatoEnriched {

    private Long id;
    private Long productoid;
    private ProductoCategory category;

    // Información adicional tomada del producto
    private String nombre;
    private String description;
    private BigInteger precio;

    // Información adicional tomada del comentario
    private float rating;
}
