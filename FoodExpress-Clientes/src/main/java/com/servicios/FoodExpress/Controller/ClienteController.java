package com.servicios.FoodExpress.Controller;

import com.servicios.FoodExpress.Model.Cliente;
import com.servicios.FoodExpress.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @Validated @RequestMapping("/api/clientes")
@Tag(name = "Cliente Controller", description = "Hace las operaciones CRUD sobre los clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Sección de los metodos GET

    /**
     * Lista todos los clientes guardados en la base de datos.
     *
     * @return Una lista de Clientes.
     */
    @GetMapping("/list-all")
    public List<Cliente> listarClientes() {
        return service.ListarTodos();
    }

    /**
     * Método para buscar un cliente por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return Un objeto Cliente si se encuentra, o null si no se encuentra.
     */
    @GetMapping("/buscar/{id}")
    public Cliente buscarClientePorId(@PathVariable Long id) {
        return service.ObtenerPorId(id);
    }

    /**
     * Método para buscar clientes por su categoría.
     *
     * @param categoria La categoría de los clientes a buscar.
     * @return Una lista de Clientes que pertenecen a la categoría especificada.
     */
    @GetMapping("/buscar/por-categoria/{categoria}")
    public List<Cliente> buscarClientesPorCategoria(@PathVariable String categoria) {
        return service.ObtenerPorCategoria(categoria);
    }

    // Sección de los metodos POST
    /**
     * Método para crear un nuevo cliente.
     *
     * @param cliente El cliente a crear.
     * @return El cliente creado.
     */
    @PostMapping("/make")
    public ResponseEntity<Cliente> crearCliente(@RequestBody @Validated Cliente cliente) {
        Cliente creado =  service.CrearCliente(cliente);
        return ResponseEntity.status(201).body(creado);
    }

    // Sección de los metodos PUT
    /**
     * Método para actualizar un cliente existente.
     *
     * @param datos El cliente con los datos actualizados.
     * @param id El ID del cliente a actualizar.
     * @return El cliente actualizado.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@RequestBody @Validated Cliente datos, @PathVariable Long id) {
        Cliente actualizado = service.ActualizarDatos(datos, id);
        return ResponseEntity.ok(actualizado);
    }

    // Sección de los metodos DELETE
    /**
     * Método para eliminar un cliente por su ID.
     *
     * @param id El ID del cliente a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Cliente> eliminarCliente(@PathVariable Long id) {
        service.EliminarCliente(id);
        return ResponseEntity.noContent().build();
    }



}
