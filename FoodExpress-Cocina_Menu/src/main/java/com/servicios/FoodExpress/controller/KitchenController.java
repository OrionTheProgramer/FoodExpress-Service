package com.servicios.FoodExpress.controller;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.model.Menu;
import com.servicios.FoodExpress.model.Plato;
import com.servicios.FoodExpress.model.PlatoEnriched;
import com.servicios.FoodExpress.service.KitchenService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController @Validated @RequestMapping("/api/kitchen")
public class KitchenController {

    private final KitchenService service;
    public KitchenController(KitchenService service) {
        this.service = service;
    }

    // Sección de Metodos GET
    @Operation(summary = "Listar todos los platos generados")
    @GetMapping("/listar/platos")
    public List<PlatoEnriched> ListarTodosLosPLatos(){
        return service.ListarPlatos();
    }

    @Operation(summary = "Listar todos los menus generados")
    @GetMapping("/listar/menus")
    public List<Menu> ListarTodosLosMenus(){
        return service.ListarMenus();
    }

    @Operation(summary = "Buscar un menu por su fecha de generacion")
    @GetMapping("/listar/menus/{date}")
    public Menu MenuPorDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.BuscarMenuPorFecha(date);
    }

    @Operation(summary = "Buscar un plato por su ID")
    @GetMapping("/listar/platos/{id}")
    public PlatoEnriched BuscarPlatoPorId(@PathVariable Long id) {
        return service.BuscarPlatoPorId(id);
    }

    // Sección de Metodos POST

    @Operation(summary = "Generar un menu con los platos del dia")
    @PostMapping("/generar/menu")
    public ResponseEntity<Menu> GenerarMenu(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                            @RequestParam Map<ProductoCategory, Integer> pedidos){
        Menu m = service.GenerarMenu(date, pedidos);

        return ResponseEntity.status(201).body(m);
    }

    @Operation(summary = "Genera y guarda platos en el sistema en base a los productos, retornando una lista de platos enriquecidos")
    @PostMapping("generar/platos")
    public ResponseEntity<List<PlatoEnriched>> GenerarPlatos(){
        List<PlatoEnriched> platos = service.GenerarPlatos();
        return ResponseEntity.status(201).body(platos);
    }

    // Sección de Metodos DELETE

    @Operation(summary = "Eliminar un plato por su ID")
    @DeleteMapping("/eliminar/platos/{id}")
    public ResponseEntity<Void> EliminarPlato(@PathVariable Long id) {
        service.EliminarPlato(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar un menu por su fecha de generacion")
    @DeleteMapping("/eliminar/menus/{date}")
    public ResponseEntity<Void> EliminarMenu(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        service.EliminarMenu(date);
        return ResponseEntity.noContent().build();
    }

}
