package com.servicios.FoodExpress;

import com.servicios.FoodExpress.Category.Status;
import com.servicios.FoodExpress.DTOs.OrdenItemDTO;
import com.servicios.FoodExpress.DTOs.OrdenRequest;
import com.servicios.FoodExpress.DTOs.OrdenResponse;
import com.servicios.FoodExpress.Model.Orden;
import com.servicios.FoodExpress.Model.OrdenLine;
import com.servicios.FoodExpress.Repository.OrdenRepository;
import com.servicios.FoodExpress.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoTest {

    @Mock
    private OrdenRepository repo;

    @Spy
    @InjectMocks
    private OrderService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void CrearOrden_valido_guardaYDevuelveDTO() {
        // preparar request
        OrdenItemDTO itemDto = OrdenItemDTO.builder()
                .platoId(100L)
                .cantidad(2)
                .build();
        OrdenRequest req = OrdenRequest.builder()
                .clienteId(50L)
                .items(List.of(itemDto))
                .build();

        // sGetDatos para cocina y cliente
        doReturn("PlatoX").when(service).GetDatos("k", 100L, "nombre");
        doReturn("10").when(service).GetDatos("k", 100L, "precio");
        doReturn("Cliente50").when(service).GetDatos("c", 50L, "nombre","email");

        // construir entidad
        OrdenLine line = OrdenLine.builder()
                .platoId(100L)
                .platoNombre("PlatoX")
                .cantidad(2)
                .build();
        Orden saved = Orden.builder()
                .id(7L)
                .clienteId(50L)
                .creationDate(LocalDate.now())
                .status(Status.PENDING)
                .lines(List.of(line))
                .build();
        when(repo.save(any(Orden.class))).thenReturn(saved);

        // ejecutar
        OrdenResponse resp = service.CrearOrden(req);

        // verificar
        assertEquals(7L, resp.getId());
        assertEquals("Cliente50", resp.getCliente());
        assertEquals(Status.PENDING.name(), resp.getStatus());
        assertEquals(1, resp.getItems().size());
        assertEquals(10, resp.getTotal());
        verify(repo).save(any(Orden.class));
    }

    @Test
    void ObtenerOrden_existente_devuelveDTO() {
        OrdenLine line = OrdenLine.builder()
                .platoId(101L).cantidad(1).build();
        Orden o = Orden.builder()
                .id(5L)
                .clienteId(60L)
                .creationDate(LocalDate.of(2025,1,1))
                .status(Status.IN_PROGRESS)
                .lines(List.of(line))
                .build();
        when(repo.findById(5L)).thenReturn(Optional.of(o));
        doReturn("P101").when(service).GetDatos("k",101L,"nombre");
        doReturn("30").when(service).GetDatos("k",101L,"precio");
        doReturn("Cli60: cli60@mail").when(service).GetDatos("c",60L,"nombre","email");

        OrdenResponse resp = service.ObtenerOrden(5L);

        assertEquals(5L, resp.getId());
        assertEquals(Status.IN_PROGRESS.name(), resp.getStatus());
        assertEquals("P101", resp.getItems().get(0).getNombre());
        assertEquals(30, resp.getItems().get(0).getPrecio());
        assertEquals(30, resp.getTotal());
        verify(repo).findById(5L);
    }

    @Test
    void ObtenerOrden_noExistente_lanzaRuntime() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.ObtenerOrden(99L));
        assertTrue(ex.getMessage().contains("Orden no encontrada con ID: 99"));
        verify(repo).findById(99L);
    }

    @Test
    void ListarOrdenes_devuelveTodos() {
        Orden o1 = Orden.builder().id(1L).clienteId(10L)
                .creationDate(LocalDate.now()).status(Status.COMPLETED)
                .lines(Collections.emptyList()).build();
        Orden o2 = Orden.builder().id(2L).clienteId(20L)
                .creationDate(LocalDate.now()).status(Status.PENDING)
                .lines(Collections.emptyList()).build();
        when(repo.findAll()).thenReturn(Arrays.asList(o1, o2));
        // stub minimal GetDatos for each
        doReturn("Cli10: x").when(service).GetDatos("c",10L,"nombre","email");
        doReturn("Cli20: y").when(service).GetDatos("c",20L,"nombre","email");

        List<OrdenResponse> list = service.ListarOrdenes();
        assertEquals(2, list.size());
        verify(repo).findAll();
    }

    @Test
    void ActualizarStatus_existente_actualizaYDevuelveDTO() {
        Orden o = Orden.builder()
                .id(3L).clienteId(30L)
                .creationDate(LocalDate.now())
                .status(Status.PENDING)
                .lines(Collections.emptyList())
                .build();
        when(repo.findById(3L)).thenReturn(Optional.of(o));
        // capture save
        when(repo.save(any(Orden.class))).thenAnswer(i -> i.getArgument(0));
        doReturn("Cli30: z").when(service).GetDatos("c",30L,"nombre","email");

        OrdenResponse updated = service.ActualizarStatus(3L, "completed");
        assertEquals(Status.COMPLETED.name(), updated.getStatus());
        ArgumentCaptor<Orden> capt = ArgumentCaptor.forClass(Orden.class);
        verify(repo).save(capt.capture());
        assertEquals(Status.COMPLETED, capt.getValue().getStatus());
    }

    @Test
    void ActualizarStatus_noExistente_lanzaRuntime() {
        when(repo.findById(77L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.ActualizarStatus(77L, "pending"));
        assertTrue(ex.getMessage().contains("Orden no encontrada con ID: 77"));
        verify(repo).findById(77L);
    }
}
