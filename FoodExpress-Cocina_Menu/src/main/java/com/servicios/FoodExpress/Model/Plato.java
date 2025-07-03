package com.servicios.FoodExpress.Model;

import com.servicios.FoodExpress.Category.ProductoCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Platos")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Plato {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotNull @Enumerated(EnumType.STRING)
    private ProductoCategory category;
}
