package com.servicios.FoodExpress.service;

import com.servicios.FoodExpress.Model.Cliente;
import com.servicios.FoodExpress.Repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {


    private final ClienteRepository repo;
    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    // Sección de los métodos CRUD

    /**
     * Lista todos los clientes
     *
     * @return Lista de todos los clientes
     */
    public List<Cliente> ListarTodos(){
        return repo.findAll();
    }

    /**
     * Encuentra un cliente por su ID
     *
     * @param id Id del cliente a buscar
     * @return El cliente encontrado
     */
    public Cliente ObtenerPorId(Long id){
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
    }

    /**
     * Busca un cliente por su categoría
     *
     * @param categoria Categoría del cliente a buscar
     * @return Lista de clientes que pertenecen a la categoría especificada
     */
    public List<Cliente> ObtenerPorCategoria(String categoria) {
        return repo.findByCategoria(categoria);
    }

    /**
     * Crea un nuevo cliente
     *
     * @param cliente Cliente a crear
     * @return El cliente creado
     */
    public Cliente CrearCliente(Cliente cliente) {
        return repo.save(cliente);
    }

    /**
     * Actualiza un cliente existente
     *
     * @param Datos Datos del cliente a actualizar
     * @param id Id del cliente a actualizar
     * @return El cliente actualizado
     */
    public Cliente ActualizarDatos(Cliente Datos, Long id) {
        if (Datos == null || id == null) {
            throw new IllegalArgumentException("Datos del cliente o ID no pueden ser nulos");
        } else if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado con id: " + id);

        }else{
            Cliente cliente = ObtenerPorId(id);
            cliente.setNombre(Datos.getNombre());
            cliente.setApellido(Datos.getApellido());
            cliente.setEmail(Datos.getEmail());
            cliente.setTelefono(Datos.getTelefono());
            cliente.setDireccion(Datos.getDireccion());
            cliente.setCategoria(Datos.getCategoria());
            return repo.save(cliente);
        }
    }

    /**
     * Elimina un cliente por su ID
     *
     * @param id Id del cliente a eliminar
     */
    public void EliminarCliente(Long id) {
        Cliente cliente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
        repo.delete(cliente);
    }
}
