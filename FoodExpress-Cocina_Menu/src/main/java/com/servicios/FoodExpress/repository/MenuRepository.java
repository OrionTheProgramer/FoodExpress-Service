package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    public Optional<Menu> findByGenerationDate(LocalDate date);
}
