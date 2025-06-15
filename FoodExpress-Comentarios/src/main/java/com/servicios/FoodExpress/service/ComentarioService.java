package com.servicios.FoodExpress.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.servicios.FoodExpress.DTOs.ComentarioInput;
import com.servicios.FoodExpress.DTOs.ComentarioOutput;
import com.servicios.FoodExpress.model.Comentario;
import com.servicios.FoodExpress.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    // Inicializando el repositorio y los WebClient necesarios
    private final ComentarioRepository repo;
    private final WebClient clienteWebClient = WebClient.builder().baseUrl("http://localhost:8002").build();
    private final WebClient productoWebClient = WebClient.builder().baseUrl("http://localhost:8001").build();

    public ComentarioService(ComentarioRepository repo) {
        this.repo = repo;
    }

    /**
     * Crea un nuevo comentario en la base de datos.
     * @param dto El objeto DTO que contiene la información del comentario a crear.
     * @return Un objeto ComentarioOutput que representa el comentario creado, incluyendo información del cliente y del producto.
     */
    public ComentarioOutput CrearComentario(ComentarioInput dto){
        // Validando que el cliente y el producto existan
        if (dto.getCliente_id() == null || dto.getProducto_id() == null) {
            throw new IllegalArgumentException("El ID del cliente o del producto no validos");
        }
        try {
            Comentario comentario = new Comentario();
            comentario.setClienteid(dto.getCliente_id());
            comentario.setProductoid(dto.getProducto_id());
            comentario.setComentario(dto.getComentario());
            comentario.setRating(dto.getRating());

            Comentario guardado = repo.save(comentario);

            return mapToOutput(guardado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Lista todos los comentarios guardados en la base de datos.
     * @return Una lista de ComentarioOutput que representa todos los comentarios.
     */
    public List<ComentarioOutput> ListarComentarios() {
        List<Comentario> comentarios = repo.findAll();
        return comentarios.stream().map(this::mapToOutput).collect(Collectors.toList());
    }

    /**
     * Busca un comentario por su ID.
     * @param id El ID del comentario a buscar.
     * @return Un objeto ComentarioOutput que representa el comentario encontrado, o null si no se encuentra.
     */
    public ComentarioOutput BuscarComentarioPorId(Long id){
        Comentario comentario = repo.findById(id).orElse(null);
        if (comentario == null) {
            // Si no se encuentra el comentario, lanzamos una excepción
            throw new IllegalArgumentException("Comentario no encontrado con ID: " + id);
        }
        return mapToOutput(comentario);
    }

    /**
     * Busca todos los comentarios de un cliente específico.
     * @param cliente_id El ID del cliente cuyos comentarios se desean buscar.
     * @return Una lista de ComentarioOutput que representa los comentarios del cliente.
     */
    public List<ComentarioOutput> BuscarComentariosPorCliente(Long cliente_id) {
        List<Comentario> comentarios = repo.findByClienteid(cliente_id);
        if (comentarios.isEmpty()) {
            // Si no se encuentran comentarios, lanzamos una excepción
            throw new IllegalArgumentException("No se encontraron comentarios para el cliente con ID: " + cliente_id);
        }
        return comentarios.stream().map(this::mapToOutput).collect(Collectors.toList());
    }

    /**
     * Busca todos los comentarios de un producto específico.
     * @param producto_id El ID del producto cuyos comentarios se desean buscar.
     * @return Una lista de ComentarioOutput que representa los comentarios del producto.
     */
    public List<ComentarioOutput> BuscarComentariosPorProducto(Long producto_id) {
        List<Comentario> comentarios = repo.findByProductoid(producto_id);
        if (comentarios.isEmpty()) {
            // Si no se encuentran comentarios, lanzamos una excepción
            throw new IllegalArgumentException("No se encontraron comentarios para el producto con ID: " + producto_id);
        }
        return comentarios.stream().map(this::mapToOutput).collect(Collectors.toList());
    }

    /**
     * Elimina un comentario por su ID.
     * @param id El ID del comentario a eliminar.
     */
    public void EliminarComentario(Long id) {
        Comentario comentario = repo.findById(id).orElse(null);
        if (comentario == null) {
            // Si no se encuentra el comentario, lanzamos una excepción
            throw new IllegalArgumentException("Comentario no encontrado con ID: " + id);
        }
        repo.delete(comentario);
    }

    private ComentarioOutput mapToOutput(Comentario comentario){
        ComentarioOutput out = new ComentarioOutput();

        out.setId(comentario.getId());
        out.setCliente_id(comentario.getClienteid());
        out.setProducto_id(comentario.getProductoid());
        out.setComentario(comentario.getComentario());
        out.setRating(comentario.getRating());
        out.setFecha_upload(comentario.getFecha_upload());

        // Llamadas a los microservicios para obtener los nombres del cliente y del producto
        String clienteNombre = GetDatos("c", comentario.getClienteid(), "nombre");
        String productoNombre = GetDatos("p", comentario.getProductoid(), "nombre");

        out.setCliente_nombre(clienteNombre);
        out.setProducto_nombre(productoNombre);
        return out;

    }

    public String ObtenerDatosParaTets(Long clienteId, Long productoId) {
        // 1) Llamada al microservicio de clientes
        String clienteDatos = clienteWebClient.get()
                .uri("/api/clientes/buscar/{id}", clienteId)
                .retrieve()
                .bodyToMono(String.class)   // <— cuerpo completo en formato JSON
                .block();                   // <— bloquea hasta recibir la respuesta

        // 2) Llamada al microservicio de productos
        String productoDatos = productoWebClient.get()
                .uri("/api/productos/buscar/{id}", productoId)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return "Cliente: " + clienteDatos + "\nProducto: " + productoDatos;
    }

    /**
     * Método que obtiene el dato de un cliente o producto según el modo especificado.
     *
     * @param mode       "c" para cliente, "p" para producto.
     * @param id         ID del cliente o producto.
     * @param WhatIWant  El campo específico que se desea obtener (ej. "nombre").
     * @return El nombre del cliente o producto solicitado.
     */
    public String GetDatos(String mode, Long id, String WhatIWant){
        String datos = "";

        // Sentencia if para determinar el modo de operación

        if (mode.equals("c")){
            datos = clienteWebClient.get().uri("/api/clientes/buscar/{id}", id)
                    .retrieve().bodyToMono(String.class).block();

        } else if (mode.equals("p")) {
            datos = productoWebClient.get().uri("/api/productos/buscar/{id}", id)
                    .retrieve().bodyToMono(String.class).block();
        } else {
            throw new IllegalArgumentException("Modo no válido. Use 'c' para cliente o 'p' para producto.");
        }

        JsonNode node;
        try {
            node = new ObjectMapper().readTree(datos);
            datos = ((ObjectNode) node).get(WhatIWant).asText();
            return datos;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información del Json", e);
        }

    }
}
