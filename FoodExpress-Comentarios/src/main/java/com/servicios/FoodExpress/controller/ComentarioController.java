package com.servicios.FoodExpress.controller;

import com.servicios.FoodExpress.DTOs.ComentarioInput;
import com.servicios.FoodExpress.DTOs.ComentarioOutput;
import com.servicios.FoodExpress.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @Validated @RequestMapping("/api/comentarios")
@Tag(name = "Comentario Controller", description = "Hace las operaciones CRUD sobre los comentarios de los productos")
public class ComentarioController {


    private final ComentarioService service;
    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    // Método de testeo para obtener información del microservicio de clientes y de productos

    @GetMapping("/test/{id_cliente}/{id_producto}")
    @Operation(summary = "Test de conexión", description = "Prueba la conexión con los microservicios de clientes y productos.")
    public ResponseEntity<String> TestDatos(@PathVariable Long id_cliente, @PathVariable Long id_producto){
        String datos = service.ObtenerDatosParaTets(id_cliente, id_producto);
        return ResponseEntity.ok(datos);
    }


    // Sección de los metodos GET
    /**
     * Lista todos los comentarios guardados en la base de datos.
     *
     * @return Una lista de ComentarioOutput que representa todos los comentarios.
     */
    @GetMapping("/list-all")
    @Operation(summary = "Listar todos los comentarios", description = "Obtiene una lista de todos los comentarios guardados en la base de datos.")
    public List<ComentarioOutput> listarComentarios() {
        return service.ListarComentarios();
    }

    /**
     * Busca un comentario por su ID.
     * @param id El ID del comentario a buscar.
     * @return Un objeto ComentarioOutput que representa el comentario encontrado, o null si no se encuentra.
     */
    @GetMapping("/buscar/{id}")
    @Operation(summary = "Buscar comentario por ID", description = "Obtiene un comentario específico por su ID.")
    public ComentarioOutput buscarComentarioPorId(@PathVariable Long id) {
        return service.BuscarComentarioPorId(id);
    }

    /**
     * Busca todos los comentarios de un cliente específico.
     * @param cliente_id El ID del cliente cuyos comentarios se desean buscar.
     * @return Una lista de ComentarioOutput que representa los comentarios del cliente.
     */
    @GetMapping("/buscar/por-cliente/{cliente_id}")
    @Operation(summary = "Buscar comentarios por ID de cliente", description = "Obtiene todos los comentarios realizados por un cliente específico.")
    public List<ComentarioOutput> buscarPorClienteID(@PathVariable Long cliente_id){
        return service.BuscarComentariosPorCliente(cliente_id);
    }

    /**
     * Busca todos los comentarios de un producto específico.
     * @param producto_id El ID del producto cuyos comentarios se desean buscar.
     * @return Una lista de ComentarioOutput que representa los comentarios del producto.
     */
    @GetMapping("/buscar/por-producto/{producto_id}")
    @Operation(summary = "Buscar comentarios por ID de producto", description = "Obtiene todos los comentarios realizados sobre un producto específico.")
    public List<ComentarioOutput> buscarPorProductoID(@PathVariable Long producto_id){
        return service.BuscarComentariosPorProducto(producto_id);
    }

    // Sección de los métodos POST
    /**
     * Guarda un nuevo comentario en la base de datos.
     * @param comentarioInput El objeto ComentarioInput que contiene la información del comentario a guardar.
     * @return Un ComentarioOutput que representa el comentario guardado.
     */
    @PostMapping("/make")
    @Operation(summary = "Crear un nuevo comentario", description = "Guarda un nuevo comentario en la base de datos.")
    public ResponseEntity<ComentarioOutput> crearComentario(@RequestBody @Validated ComentarioInput comentarioInput) {
        ComentarioOutput nuevo = service.CrearComentario(comentarioInput);
        return ResponseEntity.status(201).body(nuevo);
    }

    // Sección de los métodos DELETE
    /**
     * Elimina un comentario por su ID.
     * @param id El ID del comentario a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar comentario por ID", description = "Elimina un comentario específico por su ID.")
    public ResponseEntity<ComentarioOutput> eliminarComentario(@PathVariable Long id) {
        service.EliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
