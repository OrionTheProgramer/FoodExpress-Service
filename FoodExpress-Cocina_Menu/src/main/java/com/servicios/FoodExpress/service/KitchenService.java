package com.servicios.FoodExpress.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.model.Menu;
import com.servicios.FoodExpress.model.Plato;
import com.servicios.FoodExpress.model.PlatoEnriched;
import com.servicios.FoodExpress.repository.MenuRepository;
import com.servicios.FoodExpress.repository.PlatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KitchenService {
    // Repositorios y WebClients
    private final PlatoRepository repo_plato;
    private final MenuRepository repo_menu;
    private final WebClient ProductoWeb = WebClient.builder().baseUrl("http://3.216.175.113:8001").build();
    private final WebClient ComentarioWeb = WebClient.builder().baseUrl("http://3.216.175.113:8004").build();

    public KitchenService(PlatoRepository repo_plato, MenuRepository repo_menu) {
        this.repo_plato = repo_plato;
        this.repo_menu = repo_menu;
    }

    private record InfoPlato(Long id, String nombre, String description, BigInteger precio) {}

    private float getAverageRating(Long productoid) {
        String datos = ComentarioWeb.get()
                .uri("/api/comentarios/buscar/por-producto/{productoid}", productoid)
                .exchangeToMono(response -> {
                    if (response.statusCode().is5xxServerError() ||
                            response.statusCode().is4xxClientError()) {
                        // Si da error 4xx o 5xx, devolvemos un array vacío:
                        return Mono.just("[]");
                    }
                    // En casos 2xx devolvemos el body normalmente
                    return response.bodyToMono(String.class);
                })
                .block();

        if (datos == null || datos.isEmpty()) {
            return 0.0f;
        }

        try {
            JsonNode nodo = new ObjectMapper().readTree(datos);
            int suma = 0, count = 0;
            if (nodo.isArray()) {
                for (JsonNode c : nodo) {
                    if (c.has("rating")) {
                        suma += c.get("rating").asInt();
                        count++;
                    }
                }
            }
            return count == 0 ? 0.0f : (float) suma / count;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    private PlatoEnriched Enrichment(Plato plato) {
        InfoPlato info = ProductoWeb.get()
                .uri("/api/productos/buscar/{id}", plato.getProductoId())
                .retrieve()
                .bodyToMono(InfoPlato.class)
                .block();

        return PlatoEnriched.builder()
                .id(plato.getId())
                .productoid(plato.getProductoId())
                .category(plato.getCategory())
                .nombre(info.nombre())
                .description(info.description())
                .precio(info.precio())
                .rating(getAverageRating(plato.getProductoId()))
                .build();
    }

    public Menu GenerarMenu(LocalDate date, Map<String, Integer> categoria_cantidad) {
        Menu menu = new Menu();
        menu.setGenerationDate(date);

        List<Plato> allSelected = new ArrayList<>();
        for (var entry : categoria_cantidad.entrySet()) {
            ProductoCategory cat = ProductoCategory.valueOf(entry.getKey());
            int qty = entry.getValue();

            List<Plato> disponibles = repo_plato.findByCategory(cat);
            if (disponibles.size() < qty) {
                throw new IllegalArgumentException("No hay suficientes platos en la categoría: " + cat);
            }

            Collections.shuffle(disponibles);
            List<Plato> slice = disponibles.subList(0, qty);
            allSelected.addAll(slice);
        }

        menu.setDishes(allSelected);
        return repo_menu.save(menu);
    }

    public List<PlatoEnriched> GenerarPlatos() {
        String bruto = ProductoWeb.get()
                .uri("/api/productos/listar")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (bruto == null || bruto.isEmpty()) {
            throw new IllegalArgumentException("No hay productos disponibles para generar platos.");
        }

        try {
            JsonNode arr = new ObjectMapper().readTree(bruto);
            if (arr.isArray()) {
                for (JsonNode p : arr) {
                    if (p.has("id") && p.has("categoria")) {
                        Long pid = p.get("id").asLong();
                        // sólo crea si no existe
                        if (!repo_plato.existsByProductoId(pid)) {
                            ProductoCategory cat =
                                    ProductoCategory.valueOf(p.get("categoria").asText().toUpperCase());

                            Plato nuevo = Plato.builder()
                                    .productoId(pid)
                                    .category(cat)
                                    .build();

                            repo_plato.save(nuevo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error procesando productos JSON", e);
        }

        // ahora enriquecemos todos
        return repo_plato.findAll()
                .stream()
                .map(this::Enrichment)
                .collect(Collectors.toList());
    }

    public List<PlatoEnriched> ListarPlatos() {
        return repo_plato.findAll()
                .stream()
                .map(this::Enrichment)
                .collect(Collectors.toList());
    }

    public PlatoEnriched BuscarPlatoPorId(Long id) {
        Plato p = repo_plato.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado con ID: " + id));
        return Enrichment(p);
    }

    public List<Menu> ListarMenus() {
        return repo_menu.findAll();
    }

    public Menu BuscarMenuPorFecha(LocalDate fecha) {
        return repo_menu.findByGenerationDate(fecha)
                .orElseThrow(() -> new IllegalArgumentException("Menú no encontrado para la fecha: " + fecha));
    }

    public void EliminarPlato(Long id) {
        Plato p = repo_plato.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado con ID: " + id));
        repo_plato.delete(p);
    }

    public void EliminarMenu(LocalDate date) {
        Menu m = repo_menu.findByGenerationDate(date)
                .orElseThrow(() -> new IllegalArgumentException("Menú no encontrado con fecha: " + date));
        repo_menu.delete(m);
    }
}
