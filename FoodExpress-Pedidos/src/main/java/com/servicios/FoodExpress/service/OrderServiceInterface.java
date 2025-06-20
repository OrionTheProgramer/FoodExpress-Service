package com.servicios.FoodExpress.service;

import com.servicios.FoodExpress.DTOs.OrdenRequest;
import com.servicios.FoodExpress.DTOs.OrdenResponse;

import java.util.List;

public interface OrderServiceInterface {
    OrdenResponse CrearOrden (OrdenRequest ordenRequest);
    OrdenResponse ObtenerOrden (Long id);
    List<OrdenResponse> ListarOrdenes();
    OrdenResponse ActualizarStatus (Long id, String status);
}
