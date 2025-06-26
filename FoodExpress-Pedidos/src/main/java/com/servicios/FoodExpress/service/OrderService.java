package com.servicios.FoodExpress.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicios.FoodExpress.Category.Status;
import com.servicios.FoodExpress.DTOs.OrdenItemDTO;
import com.servicios.FoodExpress.DTOs.OrdenRequest;
import com.servicios.FoodExpress.DTOs.OrdenResponse;
import com.servicios.FoodExpress.model.Orden;
import com.servicios.FoodExpress.model.OrdenLine;
import com.servicios.FoodExpress.repository.OrdenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class OrderService implements OrderServiceInterface{

    private final OrdenRepository repo;
    private final WebClient KitchenClient = WebClient.builder().baseUrl("http://3.216.175.113:8003/api/kitchen").build();
    private final WebClient ClienteClient = WebClient.builder().baseUrl("http://3.216.175.113:8002/api/clientes").build();

    @Override @Transactional
    public OrdenResponse CrearOrden(OrdenRequest req){
        // Creando la entidad Orden
        Orden orden = Orden.builder().clienteId(req.getClienteId()).creationDate(LocalDate.now()).status(Status.PENDING)
                .lines(req.getItems().stream().map(dto ->
                        OrdenLine.builder().platoId(dto.getPlatoId()).platoNombre(GetDatos("k", dto.getPlatoId(),
                                        "nombre")).cantidad(dto.getCantidad())
                .build()).toList()).build();

        orden = repo.save(orden);

        return ToDTO(orden);
    }

    @Override
    public OrdenResponse ObtenerOrden(Long id){
        Orden o = repo.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        return ToDTO(o);
    }

    @Override
    public List<OrdenResponse> ListarOrdenes(){
        return repo.findAll().stream().map(this::ToDTO).toList();
    }

    @Override @Transactional
    public OrdenResponse ActualizarStatus(Long id, String status){
        Orden o = repo.findById(id).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        o.setStatus(Status.valueOf(status.toUpperCase()));
        o = repo.save(o);
        return ToDTO(o);
    }

    //-------------------Sección metodos privados para obtener datos o transformar datos--------------------------------

    /**
     * Método privado para transformar un Orden a un OrdenResponse.
     *
     * @param o El objeto Orden que se desea transformar.
     * @return Un OrdenResponse con los datos de la orden.
     */
    private OrdenResponse ToDTO(Orden o){
        Integer total = 0;

        var items = o.getLines().stream().map(line -> {
            var item = new OrdenItemDTO();
            item.setPlatoId(line.getPlatoId());
            item.setNombre(GetDatos("k", line.getPlatoId(), "nombre"));
            item.setPrecio(Integer.parseInt(GetDatos("k", line.getPlatoId(), "precio")));
            item.setCantidad(line.getCantidad());
            return item;
        }).toList();

        for (OrdenItemDTO item: items){
            total += item.getPrecio();
        }

        return OrdenResponse.builder().id(o.getId()).cliente(GetDatos("c", o.getClienteId(),
                "nombre", "email")).creationDate(o.getCreationDate()).status(o.StatusToString()).items(items)
                .total(total).build();
    }

    /**
     * Método para obtener datos desde un servidor remoto basado en el modo de operación y los campos deseados.
     *
     * @param mode       El modo de operación, puede ser "k" para datos de la cocina o "c" para datos del cliente.
     * @param id         El identificador del recurso que se desea consultar.
     * @param WhatIWant  Los nombres de los campos específicos que se desean extraer de la respuesta del servidor.
     * @return           Una cadena de texto con los datos solicitados en formato "campo: valor".
     * @throws RuntimeException Si ocurre un error al obtener la información del servidor o si un campo solicitado no existe.
     */
    public String GetDatos(String mode, Long id, String... WhatIWant){
        String datos = null;

        // Sentencia if para determinar el modo de operación

        if (mode.equals("k")){
            datos = KitchenClient.get().uri("/listar/platos/{id}", id).retrieve().bodyToMono(String.class)
                    .block();
        } else if (mode.equals("c")){
            datos = ClienteClient.get().uri("/buscar/{id}", id).retrieve().bodyToMono(String.class)
                    .block();
        }

        JsonNode node;
        try {
            node = new ObjectMapper().readTree(datos);
            datos = "";
            if (WhatIWant.length > 1){
                for (String what : WhatIWant) {
                    if (node.has(what)){
                        datos += what+": "+node.get(what).asText()+" ";
                    }else{
                        throw new RuntimeException("El campo '" + what + "' no existe.");
                    }
                }
                return datos;
            }else {
                datos = node.get(WhatIWant[0]).asText();
                return datos;
            }


        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la información del servidor", e);
        }

    }
}
