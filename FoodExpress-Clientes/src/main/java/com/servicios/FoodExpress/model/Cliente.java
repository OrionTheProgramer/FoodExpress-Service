package com.servicios.FoodExpress.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.servicios.FoodExpress.Category.ClienteCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CLIENTE")
public class Cliente {

    // Id del cliente
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "cliente_nombre", length = 50, nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "cliente_apellido", length = 50, nullable = false)
    private String apellido;

    @NotBlank @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email inválido")
    @Column(name = "cliente_email", length = 100, nullable = false, unique = true)
    private String email;

    // Variable de numero de telefono tomando en cuanta el formato de chile
    @Pattern(regexp = "^\\+?56\\s?9\\s?[1-9]\\d{7}$", message = "Número de teléfono inválido")
    @Column(name = "cliente_telefono", length = 15, nullable = true, unique = true)
    private String telefono;

    @NotBlank @Column(name = "cliente_direccion", length = 255, nullable = false)
    private String direccion;

    @NotNull @Column(name = "cliente_categoria", length = 23, nullable = false)
    @Enumerated(EnumType.STRING)
    private ClienteCategory categoria;

    @CreationTimestamp
    @Column(name = "fecha_join", updatable = false)
    private LocalDate fecha_join;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
