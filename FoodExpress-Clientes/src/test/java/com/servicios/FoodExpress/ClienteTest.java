package com.servicios.FoodExpress;

import com.servicios.FoodExpress.Model.Cliente;
import com.servicios.FoodExpress.Category.ClienteCategory;
import com.servicios.FoodExpress.Repository.ClienteRepository;
import com.servicios.FoodExpress.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteTest {

    @Mock
    private ClienteRepository repo;

    @InjectMocks
    private ClienteService service;

    @Test
    void ListarTodos_devuelveTodosLosClientes() {
        Cliente c1 = Cliente.builder()
                .id(1L).nombre("Ana").apellido("Perez")
                .email("ana@mail.com").telefono("1234")
                .direccion("Av. A").categoria(ClienteCategory.Comprador_VIP)
                .build();
        Cliente c2 = Cliente.builder()
                .id(2L).nombre("Luis").apellido("Gómez")
                .email("luis@mail.com").telefono("5678")
                .direccion("Calle B").categoria(ClienteCategory.Comprador_Recurrente)
                .build();

        when(repo.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Cliente> lista = service.ListarTodos();
        assertEquals(2, lista.size());
        verify(repo).findAll();
    }

    @Test
    void ObtenerPorId_existente_devuelveCliente() {
        Cliente c = Cliente.builder()
                .id(5L).nombre("Pedro").apellido("Rodríguez")
                .email("pedro@mail.com").telefono("9999")
                .direccion("Av. C").categoria(ClienteCategory.Comprador_VIP)
                .build();
        when(repo.findById(5L)).thenReturn(Optional.of(c));

        Cliente resultado = service.ObtenerPorId(5L);
        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());
        verify(repo).findById(5L);
    }

    @Test
    void ObtenerPorId_noExistente_lanzaRuntimeException() {
        when(repo.findById(42L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.ObtenerPorId(42L));
        assertTrue(ex.getMessage().contains("Cliente no encontrado con id: 42"));
        verify(repo).findById(42L);
    }

    @Test
    void ObtenerPorCategoria_devuelveSoloLosDeEsaCategoria() {
        Cliente c1 = Cliente.builder()
                .id(1L).nombre("Ana").apellido("Perez")
                .email("ana@mail.com").telefono("1234")
                .direccion("Av. A").categoria(ClienteCategory.Comprador_VIP)
                .build();
        // el service recibe String categoría, tu repo.findByCategoria debe mirar el código tras el guión bajo
        when(repo.findByCategoria("VIP")).thenReturn(Arrays.asList(c1));

        List<Cliente> vip = service.ObtenerPorCategoria("VIP");
        assertEquals(1, vip.size());
        assertEquals(ClienteCategory.Comprador_VIP, vip.get(0).getCategoria());
        verify(repo).findByCategoria("VIP");
    }

    @Test
    void CrearCliente_guardaYDevuelveElMismo() {
        Cliente nuevo = Cliente.builder()
                .nombre("María").apellido("Lopez")
                .email("maria@mail.com").telefono("2222")
                .direccion("Calle X").categoria(ClienteCategory.Comprador_Recurrente)
                .build();
        Cliente guardado = Cliente.builder()
                .id(10L)
                .nombre("María").apellido("Lopez")
                .email("maria@mail.com").telefono("2222")
                .direccion("Calle X").categoria(ClienteCategory.Comprador_Recurrente)
                .build();
        when(repo.save(nuevo)).thenReturn(guardado);

        Cliente resultado = service.CrearCliente(nuevo);
        assertEquals(10L, resultado.getId());
        assertEquals("María", resultado.getNombre());
        verify(repo).save(nuevo);
    }

    @Test
    void actualizarDatos_existente_modificaYGuarda() {
        // Mock del cliente existente
        Cliente existente = Cliente.builder()
                .id(7L)
                .nombre("Juan")
                .apellido("Ramírez")
                .email("juan@mail.com")
                .telefono("3333")
                .direccion("Av. Y")
                .categoria(ClienteCategory.Comprador_Recurrente)
                .build();

        // Cambios a aplicar
        Cliente cambios = Cliente.builder()
                .nombre("Juan Carlos")
                .apellido("Ramírez")
                .email("jc@mail.com")
                .telefono("4444")
                .direccion("Av. Z")
                .categoria(ClienteCategory.Comprador_VIP)
                .build();

        // 1) Stubear existsById y findById
        when(repo.existsById(7L)).thenReturn(true);
        when(repo.findById(7L)).thenReturn(Optional.of(existente));
        // 2) Stubear save
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Ejecutamos
        Cliente actualizado = service.ActualizarDatos(cambios, 7L);

        // Verificaciones
        assertEquals(7L, actualizado.getId());
        assertEquals("Juan Carlos", actualizado.getNombre());
        assertEquals("jc@mail.com", actualizado.getEmail());
        assertEquals(ClienteCategory.Comprador_VIP, actualizado.getCategoria());

        // Capturamos lo que llamó a repo.save()
        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);
        verify(repo).save(captor.capture());
        Cliente saved = captor.getValue();
        assertEquals("Juan Carlos", saved.getNombre());
        assertEquals("Av. Z", saved.getDireccion());

        // Comprobar que primero verifica existsById
        verify(repo).existsById(7L);
        verify(repo).findById(7L);
    }

    @Test
    void actualizarDatos_noExistente_lanzaIllegalArgumentException() {
        // Stubear existsById(false)
        when(repo.existsById(99L)).thenReturn(false);

        // Esperamos IllegalArgumentException
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.ActualizarDatos(Cliente.builder().build(), 99L)
        );
        assertTrue(ex.getMessage().contains("Cliente no encontrado con id: 99"));

        // Verificamos que no se llamó a findById ni a save
        verify(repo).existsById(99L);
        verify(repo, never()).findById(any());
        verify(repo, never()).save(any());
    }

    @Test
    void EliminarCliente_existente_loEliminaDelRepo() {
        Cliente c = Cliente.builder()
                .id(9L)
                .nombre("Carla").apellido("Santos")
                .email("carla@mail.com").telefono("5555")
                .direccion("Calle Q").categoria(ClienteCategory.Comprador_VIP)
                .build();
        when(repo.findById(9L)).thenReturn(Optional.of(c));

        assertDoesNotThrow(() -> service.EliminarCliente(9L));
        verify(repo).delete(c);
    }

    @Test
    void EliminarCliente_noExistente_lanzaRuntimeException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.EliminarCliente(99L));
        assertTrue(ex.getMessage().contains("Cliente no encontrado con id: 99"));
        verify(repo).findById(99L);
    }
}
