package com.servicios.FoodExpress.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.model.Menu;
import com.servicios.FoodExpress.model.Plato;
import com.servicios.FoodExpress.model.PlatoEnriched;
import com.servicios.FoodExpress.repository.MenuRepository;
import com.servicios.FoodExpress.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KitchenService {
    // Inicializando los repositorios y los WebClient necesarios
    private final PlatoRepository repo_plato;
    private final MenuRepository repo_menu;
    private final WebClient ProductoWeb = WebClient.builder().baseUrl("http://localhost:8001").build();
    private final WebClient ComentarioWeb = WebClient.builder().baseUrl("http://localhost:8004").build();

    public KitchenService(PlatoRepository repo_plato, MenuRepository repo_menu) {
        this.repo_plato = repo_plato;
        this.repo_menu = repo_menu;
    }

    private record InfoPlato(Long id, String nombre, String description,BigInteger precio) {}

    /**
     * Devolverá el promedio de rating de un producto para darcelo a un plato.
     * @param productoid El ID del producto del cual se quiere obtener el promedio de rating.
     * @return El promedio de rating del producto, o 0.0f si no hay comentarios.
     */
    private float getAverageRating(Long productoid) {
        int suma = 0;
        int count = 0;

        // LLenamos una lista con todos los comentarios que tenga el producto
        String Datos = ComentarioWeb.get()
                .uri("/api/comentarios/buscar/por-producto/{productoid}", productoid)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (Datos == null || Datos.isEmpty()) {
            return 0.0f; // Si no hay comentarios, retornamos 0
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode Nodo = mapper.readTree(Datos);

            if (Nodo.isArray()){
                for (JsonNode comentario : Nodo){
                    if (comentario.has("rating")){
                        suma += comentario.get("rating").asInt();
                        count += 1;
                    }
                }
            }
            float promedio = (float) suma / count;
            return promedio;

        } catch (Exception e) {
            return 0.0f;
        }
    }

    /**
     * Enriquece un plato con información adicional del producto y el promedio de rating.
     * @param plato El plato que se quiere enriquecer con información adicional.
     * @return Un Plato que contiene el plato enriquecido con información del producto y el promedio de rating.
     */
    private PlatoEnriched Enrichment(Plato plato){

        InfoPlato info = ProductoWeb.get().uri("/api/productos/buscar/{id}", plato.getProductoId()).retrieve()
                .bodyToMono(InfoPlato.class).block();

        return PlatoEnriched.builder().id(plato.getId()).productoid(plato.getProductoId()).category(plato.getCategory())
                .nombre(info.nombre()).description(info.description()).precio(info.precio()).rating(getAverageRating(plato.getProductoId()))
                .build();
    }

    public Menu GenerarMenu(LocalDate date,Map<ProductoCategory, Integer> categoria_cantidad){
        Menu menu = new Menu();

        menu.setGeneration_date(date);
        for (var entry : categoria_cantidad.entrySet()) {
            ProductoCategory category = entry.getKey();
            int cantidad = entry.getValue();

            // Obtenemos los platos de la categoría especificada
            List<Plato> platos = repo_plato.findByCategory(category);

            // Si no hay suficientes platos, lanzamos una excepción
            if (platos.size() < cantidad) {
                throw new IllegalArgumentException("No hay suficientes platos en la categoría: " + category);
            }

            Collections.shuffle(platos);
            List<Plato> select = platos.subList(0, cantidad);

            menu.setDishes(select);
            repo_menu.save(menu);
        }

        return menu;
    }

    /**
     * Genera una lista de platos enriquecidos a partir de los productos disponibles.
     * Este metodo realiza las siguientes acciones:
     * 1. Obtiene todos los productos disponibles desde un microservicio.
     * 2. Verifica si cada producto ya tiene un plato asociado en la base de datos.
     * 3. Si no existe un plato para un producto, lo crea y lo guarda en el repositorio.
     * 4. Enriquece cada plato con información adicional del producto y el promedio de rating.
     *
     * @return Una lista de objetos `PlatoEnriched` que representan los platos enriquecidos.
     * @throws IllegalArgumentException Si no hay productos disponibles para generar platos.
     * @throws RuntimeException Si ocurre un error al procesar los datos del microservicio.
     */
    public List<PlatoEnriched> GenerarPlatos(){
        // Obtenemos todos los productos, y a partir de cada uno generamos un plato.
        // Luego enriquecemos cada plato con información adicional del producto y el promedio de rating.
        // También verificamos si ese producto ya tiene un plato asociado, si no lo tiene, lo creamos
        JsonNode Nodo;
        List<PlatoEnriched> platosEnriquecidos = new ArrayList<>();
        String ContadorBruto = ProductoWeb.get().uri("/api/productos/listar").retrieve().bodyToMono(String.class)
                .block();

        if (ContadorBruto == null || ContadorBruto.isEmpty()) {
            throw new IllegalArgumentException("No hay productos disponibles para generar platos.");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Nodo = mapper.readTree(ContadorBruto);

            if (Nodo.isArray()){
                for (JsonNode producto: Nodo){
                    if (producto.has("id")){
                        Long productoid = producto.get("id").asLong();
                        // Verificamos si ya existe un plato para este producto
                        if (!repo_plato.existsByProductoid(productoid)) {
                            // Si no existe, creamos un nuevo plato
                            Plato plato = Plato.builder()
                                    .productoId(productoid)
                                    .category(ProductoCategory.valueOf(producto.get("category").asText()))
                                    .build();
                            repo_plato.save(plato);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Plato> platos = repo_plato.findAll();
        platosEnriquecidos = platos.stream().map(this::Enrichment).toList();

        return platosEnriquecidos;
    }

    public List<PlatoEnriched> ListarPlatos() {
        // Listamos todos los platos enriquecidos
        List<Plato> platos = repo_plato.findAll();
        return platos.stream().map(this::Enrichment).collect(Collectors.toList());
    }

    public PlatoEnriched BuscarPlatoPorId(Long id) {
        // Buscamos un plato por su ID y lo enriquecemos
        Plato plato = repo_plato.findById(id).orElse(null);
        if (plato == null) {
            throw new IllegalArgumentException("Plato no encontrado con ID: " + id);
        }
        return Enrichment(plato);
    }

    public List<Menu> ListarMenus() {
        // Listamos todos los menús generados
        return repo_menu.findAll();
    }

    public Menu BuscarMenuPorFecha(LocalDate fecha) {
        // Buscamos un menú por su fecha de generación
        return repo_menu.findByGeneration_date(fecha)
                .orElseThrow(() -> new IllegalArgumentException("Menú no encontrado para la fecha: " + fecha));
    }

    public void EliminarPlato(Long id) {
        // Eliminamos un plato por su ID
        Plato plato = repo_plato.findById(id).orElse(null);
        if (plato == null) {
            throw new IllegalArgumentException("Plato no encontrado con ID: " + id);
        }
        repo_plato.delete(plato);
    }

    public void EliminarMenu(LocalDate date) {
        // Eliminamos un menú por su ID
        Menu menu = repo_menu.findByGeneration_date(date).orElse(null);
        if (menu == null) {
            throw new IllegalArgumentException("Menú no encontrado con ID: " + date);
        }
        repo_menu.delete(menu);
    }

}
