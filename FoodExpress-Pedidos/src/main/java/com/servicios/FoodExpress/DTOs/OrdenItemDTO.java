package com.servicios.FoodExpress.DTOs;

import lombok.Data;

@Data
public class OrdenItemDTO {

    private Long platoId;
    private String nombre;
    private Integer precio;
    private Integer cantidad;
}
