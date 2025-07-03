package com.servicios.FoodExpress.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class OrdenRequest {
    private Long clienteId;
    private List<OrdenItemDTO> items;
}
