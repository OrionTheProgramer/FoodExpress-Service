package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    public List<Plato> findByCategory(ProductoCategory category);
    public Plato findByProductoId(Long productoId);
    public Boolean existsByProductoId(Long productoId);
}
