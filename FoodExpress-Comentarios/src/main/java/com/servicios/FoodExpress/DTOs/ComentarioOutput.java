package com.servicios.FoodExpress.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Clase que representa la salida de un comentario.
 * Contiene información sobre el cliente, el producto, el comentario y la fecha de subida.
 */
@Data @Builder
public class ComentarioOutput {
    private Long id;

    private Long cliente_id;
    private String cliente_nombre;

    private Long producto_id;
    private String producto_nombre;

    private String comentario;
    private Integer rating;
    private LocalDate fecha_upload;
}
