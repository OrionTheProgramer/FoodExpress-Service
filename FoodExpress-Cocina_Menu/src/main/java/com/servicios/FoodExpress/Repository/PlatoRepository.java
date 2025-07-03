package com.servicios.FoodExpress.Repository;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.Model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    public List<Plato> findByCategory(ProductoCategory category);
    public Plato findByProductoId(Long productoId);
    public Boolean existsByProductoId(Long productoId);
}
