package com.servicios.FoodExpress.controller;

import com.servicios.FoodExpress.DTOs.OrdenRequest;
import com.servicios.FoodExpress.DTOs.OrdenResponse;
import com.servicios.FoodExpress.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/ordenes") @RequiredArgsConstructor @Validated
@Tag(name = "Orden Controller", description = "Maneja las operaciones CRUD sobre las órdenes de los clientes")
public class OrdenController {

    private final OrderService service;

    // Sección de los métodos GET

    @GetMapping("/listar")
    public ResponseEntity<List<OrdenResponse>> ListarOrdenes(){
        return ResponseEntity.status(201).body(service.ListarOrdenes());
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<OrdenResponse> ObtenerPorId(@PathVariable @Validated Long id){
        return ResponseEntity.status(201).body(service.ObtenerOrden(id));
    }

    // Sección de los métodos POST

    @PostMapping("/make-order")
    public ResponseEntity<OrdenResponse> CrearOrden(@RequestBody @Validated OrdenRequest req){
        OrdenResponse response = service.CrearOrden(req);
        return ResponseEntity.status(201).body(response);
    }

    // Sección de los métodos PUT

    @PutMapping("/update-status/{id}")
    public ResponseEntity<OrdenResponse> ActualizarStatus(@PathVariable @Validated Long id, @RequestParam String status){
        OrdenResponse response = service.ActualizarStatus(id, status);
        return ResponseEntity.status(201).body(response);
    }
}
