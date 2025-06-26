package com.servicios.FoodExpress.Config;

import com.servicios.FoodExpress.Category.ClienteCategory;
import com.servicios.FoodExpress.model.Cliente;
import com.servicios.FoodExpress.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Carga datos de prueba al iniciar la aplicación.
 * Solo se activa en el perfil 'dev'.
 */

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository repo;


    @Override
    public void run(String... args) throws Exception {

        // Generador de datos falsos
        Faker faker = new Faker();

        // Borra todo
        repo.deleteAll();

        // Crea productos de prueba
        for (int i = 0; i <= 20; i++) {
            Cliente cliente = new Cliente();
            cliente.setNombre(faker.name().name());
            cliente.setApellido(faker.name().lastName());
            cliente.setEmail(faker.internet().emailAddress());

            // Genera un número de teléfono aleatorio
            String PhoneNumber = String.format("+569%08d",
                    faker.number().numberBetween(10_000_000L, 100_000_000L));
            cliente.setTelefono(PhoneNumber);



            // Aleatoriamente, elige una categoría según ClienteCategory
            List<ClienteCategory> categorias = List.of(ClienteCategory.values());
            cliente.setCategoria(categorias.get(faker.number().numberBetween(0, categorias.size())));
            cliente.setDireccion(faker.address().fullAddress());

            // Guarda el cliente
            repo.save(cliente);

        }

        System.out.println("=== Clientes de prueba cargados ===");
        repo.findAll().forEach(System.out::println);
    }
}
