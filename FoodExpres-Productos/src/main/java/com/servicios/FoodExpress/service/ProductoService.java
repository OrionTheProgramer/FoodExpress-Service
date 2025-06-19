package com.servicios.FoodExpress.service;

import com.servicios.FoodExpress.model.Producto;
import com.servicios.FoodExpress.repository.ProductoRepository;
/*import org.springframework.beans.factory.annotation.Autowired; cuando tenga mas de un contructor se debe utilizar*/
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio para manejar la logica de los productos.
 * 
 * @author Leandro
 */
@Service
public class ProductoService {

    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    // Sección de métodos para manejar productos

    /**
     * Lista todos los productos guardados en la base de datos.
     *
     * @return Una lista de objetos Producto que representan todos los productos.
     */
    public List<Producto> ListarProductos() {
        return repo.findAll();
    }

    /**
     * Metodo para encontrar un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return Un objeto Producto si se encuentra, o null si no se encuentra.
     */
    public Producto BuscarProductoPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Metodo para encontrar productos por su categoría.
     *
     * @param categoria La categoría de los productos a buscar.
     * @return Una lista de objetos Producto que pertenecen a la categoría especificada.
     */
    public List<Producto> BuscarProductoPorCategoria(String categoria) {
        return repo.findByCategoria(categoria);
    }

    /**
     * Metodo para encontrar productos cuyo precio sea menor o igual al precio máximo especificado.
     *
     * @param precioMaximo El precio máximo para filtrar los productos.
     * @return Una lista de objetos Producto cuyo precio es menor o igual al precio máximo especificado.
     */
    public List<Producto> BuscarProductoPorPrecioMaximo(Long precioMaximo) {
        return repo.findByPrecioLessThanEqual(precioMaximo);
    }

    /**
     * Metodo para guardar un nuevo producto en la base de datos.
     *
     * @param producto El objeto Producto a guardar.
     * @return El objeto Producto guardado.
     */
    public Producto CrearProducto(Producto producto) {
        return repo.save(producto);
    }

    /**
     * Metodo para actualizar un producto existente en la base de datos.
     *
     * @param Datos El objeto Producto con los datos actualizados.
     * @param id El ID del producto a actualizar.
     * @return El objeto Producto actualizado.
     */
    public Producto ActualizarProducto(Producto Datos, Long id) {
        try {
            Producto exis = BuscarProductoPorId(id);
            if (exis != null) {

                exis.setNombre(Datos.getNombre());
                exis.setDescription(Datos.getDescription());
                exis.setPrecio(Datos.getPrecio());
                exis.setStock(Datos.getStock());
                exis.setCategoria(Datos.getCategoria());

                return repo.save(exis);
            } else {
                return null; // Producto no encontrado
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Manejo de excepciones
        }
    }

    /**
     * Metodo para eliminar un producto de la base de datos por su ID.
     *
     * @param id El ID del producto a eliminar.
     */
    public void EliminarProducto(Long id) {
        try {
            Producto exis = BuscarProductoPorId(id);
            if (exis != null) {
                repo.delete(exis);
            } else {
                throw new RuntimeException("Producto no encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el producto: " + e.getMessage());
        }
    }
}
