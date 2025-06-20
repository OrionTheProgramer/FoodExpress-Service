package com.servicios.FoodExpress.model;

import com.servicios.FoodExpress.Category.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity @Table(name = "Ordenes")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(name = "creation_date", updatable = false, nullable = false)
    private LocalDate creationDate;

    @ElementCollection
    @CollectionTable(name = "orden_lines", joinColumns = @JoinColumn(name = "orden_id"))
    private List<OrdenLine> lines;


    public String StatusToString() {
        return status != null ? status.toString() : "UNKNOWN";
    }
}
