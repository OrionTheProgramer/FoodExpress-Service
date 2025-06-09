package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz para detectar cuando un producto,
 * tiene bajo stock, sirve para encontrar productos, desactivar o eliminar
 * productos.
 * 
 * @author Leandro
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    public List<Producto> findByCategoria(String categoria);
    public List<Producto> findByPrecioLessThanEqual(Long precioMaximo);
}
