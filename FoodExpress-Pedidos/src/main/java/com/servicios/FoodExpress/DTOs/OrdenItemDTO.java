package com.servicios.FoodExpress.DTOs;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OrdenItemDTO {

    private Long platoId;
    private String nombre;
    private Integer precio;
    private Integer cantidad;
}
