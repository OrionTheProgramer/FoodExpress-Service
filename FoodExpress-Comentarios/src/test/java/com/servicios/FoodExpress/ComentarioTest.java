package com.servicios.FoodExpress;

import com.servicios.FoodExpress.DTOs.ComentarioInput;
import com.servicios.FoodExpress.DTOs.ComentarioOutput;
import com.servicios.FoodExpress.Model.Comentario;
import com.servicios.FoodExpress.Repository.ComentarioRepository;
import com.servicios.FoodExpress.service.ComentarioService;
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
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository repo;

    @Spy
    @InjectMocks
    private ComentarioService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void crearComentario_valido_guardaYDevuelveOutput() {
        // dado un DTO de entrada
        ComentarioInput dto = ComentarioInput.builder()
                .cliente_id(1L)
                .producto_id(2L)
                .comentario("Buen plato")
                .rating(5)
                .build();

        // y el repo devuelve un Comentario con ID y fecha
        Comentario saved = Comentario.builder()
                .id(10L)
                .clienteid(1L)
                .productoid(2L)
                .comentario("Buen plato")
                .rating(5)
                .fecha_upload(LocalDate.now())
                .build();
        when(repo.save(any(Comentario.class))).thenReturn(saved);

        // py para GetDatos
        doReturn("ClienteX").when(service).GetDatos("c", 1L, "nombre");
        doReturn("ProductoY").when(service).GetDatos("p", 2L, "nombre");

        ComentarioOutput out = service.CrearComentario(dto);

        assertEquals(10L, out.getId());
        assertEquals(1L, out.getCliente_id());
        assertEquals(2L, out.getProducto_id());
        assertEquals("Buen plato", out.getComentario());
        assertEquals(5, out.getRating());
        assertEquals("ClienteX", out.getCliente_nombre());
        assertEquals("ProductoY", out.getProducto_nombre());

        ArgumentCaptor<Comentario> capt = ArgumentCaptor.forClass(Comentario.class);
        verify(repo).save(capt.capture());
        assertEquals("Buen plato", capt.getValue().getComentario());
    }

    @Test
    void listarComentarios_devuelveListaEnriquecida() {
        Comentario c1 = Comentario.builder()
                .id(1L).clienteid(1L).productoid(2L)
                .comentario("OK").rating(3)
                .fecha_upload(LocalDate.now())
                .build();
        when(repo.findAll()).thenReturn(Arrays.asList(c1));

        doReturn("Cli1").when(service).GetDatos("c",1L,"nombre");
        doReturn("Prod2").when(service).GetDatos("p",2L,"nombre");

        List<ComentarioOutput> lista = service.ListarComentarios();
        assertEquals(1, lista.size());
        ComentarioOutput out = lista.get(0);
        assertEquals("OK", out.getComentario());
        assertEquals("Cli1", out.getCliente_nombre());
        assertEquals("Prod2", out.getProducto_nombre());
        verify(repo).findAll();
    }

    @Test
    void buscarPorId_existente_devuelveOutput() {
        Comentario c = Comentario.builder()
                .id(5L).clienteid(10L).productoid(20L)
                .comentario("Test").rating(4)
                .fecha_upload(LocalDate.now())
                .build();
        when(repo.findById(5L)).thenReturn(Optional.of(c));
        doReturn("C10").when(service).GetDatos("c",10L,"nombre");
        doReturn("P20").when(service).GetDatos("p",20L,"nombre");

        ComentarioOutput out = service.BuscarComentarioPorId(5L);
        assertEquals(5L, out.getId());
        assertEquals("C10", out.getCliente_nombre());
        assertEquals("P20", out.getProducto_nombre());
        verify(repo).findById(5L);
    }

    @Test
    void buscarPorId_noExistente_lanzaIAE() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.BuscarComentarioPorId(99L)
        );
        assertTrue(ex.getMessage().contains("Comentario no encontrado con ID: 99"));
        verify(repo).findById(99L);
    }

    @Test
    void buscarPorCliente_existente_devuelveLista() {
        Comentario c = Comentario.builder()
                .id(2L).clienteid(7L).productoid(3L)
                .comentario("X").rating(2)
                .fecha_upload(LocalDate.now())
                .build();
        when(repo.findByClienteid(7L)).thenReturn(Collections.singletonList(c));
        doReturn("Name7").when(service).GetDatos("c",7L,"nombre");
        doReturn("Prod3").when(service).GetDatos("p",3L,"nombre");

        List<ComentarioOutput> out = service.BuscarComentariosPorCliente(7L);
        assertEquals(1, out.size());
        assertEquals(7L, out.get(0).getCliente_id());
        verify(repo).findByClienteid(7L);
    }

    @Test
    void buscarPorCliente_sinResultados_lanzaIAE() {
        when(repo.findByClienteid(5L)).thenReturn(Collections.emptyList());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.BuscarComentariosPorCliente(5L)
        );
        assertTrue(ex.getMessage().contains("No se encontraron comentarios para el cliente con ID: 5"));
        verify(repo).findByClienteid(5L);
    }

    @Test
    void buscarPorProducto_existente_devuelveLista() {
        Comentario c = Comentario.builder()
                .id(4L).clienteid(8L).productoid(9L)
                .comentario("Y").rating(1)
                .fecha_upload(LocalDate.now())
                .build();
        when(repo.findByProductoid(9L)).thenReturn(Collections.singletonList(c));
        doReturn("C8").when(service).GetDatos("c",8L,"nombre");
        doReturn("P9").when(service).GetDatos("p",9L,"nombre");

        List<ComentarioOutput> out = service.BuscarComentariosPorProducto(9L);
        assertEquals(1, out.size());
        assertEquals(9L, out.get(0).getProducto_id());
        verify(repo).findByProductoid(9L);
    }

    @Test
    void buscarPorProducto_sinResultados_lanzaIAE() {
        when(repo.findByProductoid(3L)).thenReturn(Collections.emptyList());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.BuscarComentariosPorProducto(3L)
        );
        assertTrue(ex.getMessage().contains("No se encontraron comentarios para el producto con ID: 3"));
        verify(repo).findByProductoid(3L);
    }

    @Test
    void eliminarComentario_existente_noLanza() {
        Comentario c = Comentario.builder().id(11L).build();
        when(repo.findById(11L)).thenReturn(Optional.of(c));

        assertDoesNotThrow(() -> service.EliminarComentario(11L));
        verify(repo).delete(c);
    }

    @Test
    void eliminarComentario_noExistente_lanzaIAE() {
        when(repo.findById(22L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.EliminarComentario(22L)
        );
        assertTrue(ex.getMessage().contains("Comentario no encontrado con ID: 22"));
        verify(repo).findById(22L);
    }
}
