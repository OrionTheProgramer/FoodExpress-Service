package com.servicios.FoodExpress.Repository;

import com.servicios.FoodExpress.Model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
