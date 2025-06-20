package com.servicios.FoodExpress.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data @Builder
public class OrdenResponse {
    private Long id;
    private String cliente;
    private LocalDate creationDate;
    private String status;
    private List<OrdenItemDTO> items;
    private Integer total;
}
