package com.servicios.FoodExpress.controller;

import com.servicios.FoodExpress.model.Producto;
import com.servicios.FoodExpress.service.ProductoService;
/*import org.springframework.beans.factory.annotation.Autowired; cuando tenga mas de un contructor se debe utilizar*/
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para gestionar los productos.
 * 
 * @author Leandro
 */
@RestController @Validated @RequestMapping("/api/productos")
@Tag(name = "Producto Controller", description = "Hace las operaciones CRUD sobre los productos")
public class ProductoController {

    private final ProductoService service;
    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // Sección de los metodos GET

    /**
     * Lista todos los productos guardados en la base de datos.
     *
     * @return Una lista de Productos.
     */
    @GetMapping("/listar")
    public List<Producto> listarProductos() {
        return service.ListarProductos();
    }

    /**
     * Método para listar por la categoria del producto.
     *
     * @param Categoria La categoría de los productos a buscar.
     * @return Una lista de Productos que pertenecen a la categoría especificada.
     */
    @GetMapping("/listar/por-categoria/{categoria}")
    public List<Producto> ListarPorCategoria(@PathVariable String Categoria){
        return service.BuscarProductoPorCategoria(Categoria);
    }

    /**
     * Método para listar por el precio máximo del producto.
     *
     * @param precioMaximo El precio máximo para filtrar los productos.
     * @return Una lista de Productos cuyo precio es menor o igual al precio máximo especificado.
     */
    @GetMapping("/listar/por-precio-maximo/{precioMaximo}")
    public List<Producto> ListarPorPrecioMaximo(@PathVariable Long precioMaximo) {
        return service.BuscarProductoPorPrecioMaximo(precioMaximo);
    }

    /**
     * Método para buscar un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return Un objeto Producto si se encuentra, o null si no se encuentra.
     */
    @GetMapping("/buscar/{id}")
    public Producto BuscarProductoPorId(@PathVariable Long id) {
        return service.BuscarProductoPorId(id);
    }

    // Sección de los métodos POST

    /**
     * Método para guardar un nuevo producto en la base de datos.
     *
     * @param producto El objeto Producto a guardar.
     * @return El objeto Producto guardado.
     */
    @PostMapping("/make")
    public Producto GuardarProducto(@RequestBody Producto producto) {
        return service.GuardarProducto(producto);
    }

    // Sección de los métodos PUT

    /**
     * Método para actualizar un producto existente.
     *
     * @param id El ID del producto a actualizar.
     * @param producto El objeto Producto con los nuevos datos.
     * @return El Producto actualizado.
     */
    @PutMapping("/update/{id}")
    public Producto ActualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return service.ActualizarProducto(producto, id);
    }

    // Sección de los métodos DELETE

    /**
     * Método para eliminar un producto por su ID.
     *
     * @param id El ID del producto a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    public void EliminarProducto(@PathVariable Long id) {
        service.EliminarProducto(id);
    }
}
