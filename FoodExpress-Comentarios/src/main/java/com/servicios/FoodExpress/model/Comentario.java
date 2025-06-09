package com.servicios.FoodExpress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "Comentarios")
public class Comentario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comentario_id")
    private Long id;

    @NotNull(message = "No puede ser null")
    @Column(name = "cliente_id", nullable = false)
    private Long clienteid;

    @NotNull(message = "No puede ser null")
    @Column(name = "producto_id", nullable = false)
    private Long productoid;

    @NotBlank(message = "El comentario no puede estar vació")
    @Column(name = "comentario", length = 500, nullable = false)
    private String comentario;

    @NotNull(message = "La calificación no puede ser null")
    @Min(1) @Max(6)
    @Column(name = "calificación", nullable = false)
    private Integer rating;

    @CreationTimestamp
    @Column(name = "fecha_upload", updatable = false)
    private LocalDate fecha_upload;
}
