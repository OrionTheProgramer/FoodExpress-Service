package com.servicios.FoodExpress.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Clase que representa la entrada de un comentario.
 * Contiene información sobre el cliente, el producto, el comentario y la calificación.
 */
@Data @Builder
public class ComentarioInput {

    @NotNull(message = "El ID del cliente no puede ser null")
    private Long cliente_id;
    @NotNull(message = "El ID del producto no puede ser null")
    private Long producto_id;

    @NotBlank(message = "El comentario no puede estar vació")
    private String comentario;

    @NotNull(message = "La calificación no puede ser null")
    @Min(1) @Max(6)
    private Integer rating;

}
