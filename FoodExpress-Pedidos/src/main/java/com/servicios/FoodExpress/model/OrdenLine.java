package com.servicios.FoodExpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class OrdenLine {

    @Column(name = "plato_id", nullable = false)
    private Long platoId;

    @Column(name = "plato_nombre", nullable = false)
    private String platoNombre;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
