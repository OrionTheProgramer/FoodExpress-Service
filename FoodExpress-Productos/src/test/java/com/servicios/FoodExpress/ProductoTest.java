package com.servicios.FoodExpress;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.Model.Producto;
import com.servicios.FoodExpress.Repository.ProductoRepository;
import com.servicios.FoodExpress.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoTest {

    @Mock
    private ProductoRepository repo;

    @InjectMocks
    private ProductoService service;

    private Producto makeProducto(Long id, String nombre, long precio, int stock, ProductoCategory cat) {
        Producto p = new Producto();
        p.setId(id);
        p.setNombre(nombre);
        p.setPrecio(BigInteger.valueOf(precio));
        p.setStock(stock);
        p.setCategoria(cat);
        return p;
    }

    @Test
    void listarProductos_debeRetornarTodos() {
        // Arrange
        Producto p1 = makeProducto(1L, "Pizza", 10, 5, ProductoCategory.PIZZA);
        Producto p2 = makeProducto(2L, "Bebida", 3, 10, ProductoCategory.BEBIDA);
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        List<Producto> list = service.ListarProductos();

        // Assert
        assertEquals(2, list.size());
        verify(repo).findAll();
    }

    @Test
    void buscarPorId_existe_debeRetornarProducto() {
        // Arrange
        Producto p = makeProducto(5L, "Empanada", 2, 20, ProductoCategory.OTRO);
        when(repo.findById(5L)).thenReturn(Optional.of(p));

        // Act
        Producto found = service.BuscarProductoPorId(5L);

        // Assert
        assertNotNull(found);
        assertEquals(5L, found.getId());
        verify(repo).findById(5L);
    }

    @Test
    void buscarPorId_noExiste_lanzaException() {
        // Arrange
        when(repo.findById(99L)).thenReturn(Optional.empty());

        // Act / Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.BuscarProductoPorId(99L)
        );
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
        verify(repo).findById(99L);
    }

    @Test
    void crearProducto_guardaYRetorna() {
        // Arrange
        Producto input = makeProducto(null, "Taco", 4, 15, ProductoCategory.OTRO);
        Producto saved = makeProducto(10L, "Taco", 4, 15, ProductoCategory.OTRO);
        when(repo.save(input)).thenReturn(saved);

        // Act
        Producto result = service.CrearProducto(input);

        // Assert
        assertEquals(10L, result.getId());
        assertEquals("Taco", result.getNombre());
        verify(repo).save(input);
    }

    @Test
    void actualizarProducto_modificaYGuarda() {
        // Arrange
        Producto existing = makeProducto(20L, "Old", 5, 5, ProductoCategory.PIZZA);
        Producto cambios   = makeProducto(null, "New", 7, 8, ProductoCategory.BEBIDA);
        when(repo.findById(20L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Producto updated = service.ActualizarProducto(cambios, 20L);

        // Assert
        assertEquals("New", updated.getNombre());
        assertEquals(BigInteger.valueOf(7), updated.getPrecio());
        assertEquals(8, updated.getStock());
        assertEquals(ProductoCategory.BEBIDA, updated.getCategoria());

        ArgumentCaptor<Producto> cap = ArgumentCaptor.forClass(Producto.class);
        verify(repo).save(cap.capture());
        assertEquals("New", cap.getValue().getNombre());
    }

    @Test
    void eliminarProducto_eliminaCuandoExiste() {
        // Arrange
        Producto p = makeProducto(30L, "Queso", 2, 50, ProductoCategory.OTRO);
        when(repo.findById(30L)).thenReturn(Optional.of(p));

        // Act
        assertDoesNotThrow(() -> service.EliminarProducto(30L));

        // Assert
        verify(repo).delete(p);
    }

    @Test
    void eliminarProducto_siNoExiste_lanzaException() {
        // Arrange
        when(repo.findById(77L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.EliminarProducto(77L)
        );
        assertTrue(ex.getMessage().contains("Plato no encontrado") || ex.getMessage().contains("Producto"));
        verify(repo).findById(77L);
    }
}
