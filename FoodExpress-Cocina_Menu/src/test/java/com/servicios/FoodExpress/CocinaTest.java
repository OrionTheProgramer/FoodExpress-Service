package com.servicios.FoodExpress;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.Model.Menu;
import com.servicios.FoodExpress.Model.Plato;
import com.servicios.FoodExpress.Repository.MenuRepository;
import com.servicios.FoodExpress.Repository.PlatoRepository;
import com.servicios.FoodExpress.service.KitchenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KitchenServiceTest {

    @Mock
    private PlatoRepository repoPlato;

    @Mock
    private MenuRepository repoMenu;

    @InjectMocks
    private KitchenService service;

    private final LocalDate TODAY = LocalDate.of(2025,6,20);

    @BeforeEach
    void setUp() {
        // no-op, MockitoExtension se encarga
    }

    @Test
    void generarMenu_conCantidadSuficiente_guardaYRetornaMenu() {
        // Preparar 3 platos de categoría PIZZA y 2 de BEBIDA
        List<Plato> pizzas = Arrays.asList(
                Plato.builder().id(1L).productoId(11L).category(ProductoCategory.PIZZA).build(),
                Plato.builder().id(2L).productoId(12L).category(ProductoCategory.PIZZA).build(),
                Plato.builder().id(3L).productoId(13L).category(ProductoCategory.PIZZA).build()
        );
        List<Plato> bebidas = Arrays.asList(
                Plato.builder().id(4L).productoId(21L).category(ProductoCategory.BEBIDA).build(),
                Plato.builder().id(5L).productoId(22L).category(ProductoCategory.BEBIDA).build()
        );

        when(repoPlato.findByCategory(ProductoCategory.PIZZA)).thenReturn(pizzas);
        when(repoPlato.findByCategory(ProductoCategory.BEBIDA)).thenReturn(bebidas);

        // Map<"PIZZA"->2, "BEBIDA"->1>
        Map<String,Integer> peticiones = new HashMap<>();
        peticiones.put("PIZZA", 2);
        peticiones.put("BEBIDA", 1);

        // Capturar el menú que se guarda
        ArgumentCaptor<Menu> captMenu = ArgumentCaptor.forClass(Menu.class);
        when(repoMenu.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Menu resultado = service.GenerarMenu(TODAY, peticiones);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(TODAY, resultado.getGenerationDate());
        // Debe haber exactamente 3 platos (2 pizzas + 1 bebida)
        assertEquals(3, resultado.getDishes().size());

        verify(repoPlato).findByCategory(ProductoCategory.PIZZA);
        verify(repoPlato).findByCategory(ProductoCategory.BEBIDA);
        verify(repoMenu, times(1)).save(captMenu.capture());
        // El objeto guardado en repoMenu tiene la misma lista
        assertEquals(resultado.getDishes(), captMenu.getValue().getDishes());
    }

    @Test
    void generarMenu_conCantidadInsuficiente_lanzaError() {
        when(repoPlato.findByCategory(ProductoCategory.HAMBURGUESA))
                .thenReturn(Collections.singletonList(
                        Plato.builder().category(ProductoCategory.HAMBURGUESA).build()
                ));

        Map<String,Integer> pedido = Map.of("HAMBURGUESA", 2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.GenerarMenu(TODAY, pedido)
        );
        assertTrue(ex.getMessage().contains("No hay suficientes platos"));
        verify(repoPlato).findByCategory(ProductoCategory.HAMBURGUESA);
        verify(repoMenu, never()).save(any());
    }

    @Test
    void listarMenus_devuelveTodos() {
        Menu m1 = Menu.builder().generationDate(TODAY).build();
        Menu m2 = Menu.builder().generationDate(TODAY.plusDays(1)).build();
        when(repoMenu.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Menu> todos = service.ListarMenus();
        assertEquals(2, todos.size());
        verify(repoMenu).findAll();
    }

    @Test
    void buscarMenuPorFecha_existente_devuelveMenu() {
        Menu m = Menu.builder().generationDate(TODAY).build();
        when(repoMenu.findByGenerationDate(TODAY)).thenReturn(Optional.of(m));

        Menu encontrado = service.BuscarMenuPorFecha(TODAY);
        assertSame(m, encontrado);
        verify(repoMenu).findByGenerationDate(TODAY);
    }

    @Test
    void buscarMenuPorFecha_noExistente_lanzaError() {
        when(repoMenu.findByGenerationDate(TODAY)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.BuscarMenuPorFecha(TODAY)
        );
        assertTrue(ex.getMessage().contains("Menú no encontrado para la fecha"));
        verify(repoMenu).findByGenerationDate(TODAY);
    }

    @Test
    void eliminarPlato_existente_eliminaCorrectamente() {
        Plato p = Plato.builder().id(99L).build();
        when(repoPlato.findById(99L)).thenReturn(Optional.of(p));

        assertDoesNotThrow(() -> service.EliminarPlato(99L));
        verify(repoPlato).delete(p);
    }

    @Test
    void eliminarPlato_noExistente_lanzaError() {
        when(repoPlato.findById(7L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.EliminarPlato(7L)
        );
        assertTrue(ex.getMessage().contains("Plato no encontrado con ID"));
        verify(repoPlato).findById(7L);
    }

    @Test
    void eliminarMenu_existente_eliminaCorrectamente() {
        Menu m = Menu.builder().generationDate(TODAY).build();
        when(repoMenu.findByGenerationDate(TODAY)).thenReturn(Optional.of(m));

        assertDoesNotThrow(() -> service.EliminarMenu(TODAY));
        verify(repoMenu).delete(m);
    }

    @Test
    void eliminarMenu_noExistente_lanzaError() {
        when(repoMenu.findByGenerationDate(TODAY)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.EliminarMenu(TODAY)
        );
        assertTrue(ex.getMessage().contains("Menú no encontrado"));
        verify(repoMenu).findByGenerationDate(TODAY);
    }
}
