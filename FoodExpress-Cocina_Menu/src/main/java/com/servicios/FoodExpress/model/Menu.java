package com.servicios.FoodExpress.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Menus")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Menu {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generation_date", updatable = false, unique = true)
    private LocalDate generationDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")   // aquí le decimos “la FK menu_id vive en la tabla Platos”
    private List<Plato> dishes;
}
