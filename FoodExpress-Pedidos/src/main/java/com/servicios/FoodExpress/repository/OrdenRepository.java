package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
