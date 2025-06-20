package com.servicios.FoodExpress.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class OrdenRequest {
    private Long clienteId;
    private List<OrdenItemDTO> items;
}
