package com.servicios.FoodExpress.config;

import com.servicios.FoodExpress.model.Comentario;
import com.servicios.FoodExpress.repository.ComentarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Carga datos de prueba al iniciar la aplicación.
 * Solo se activa en el perfil 'dev'.
 */

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ComentarioRepository repo;


    @Override
    public void run(String... args) throws Exception {

        // Generador de datos falsos
        Faker faker = new Faker();

        // Borra todo
        repo.deleteAll();

        // Crea productos de prueba
        for (int i = 0; i <= 70; i++) {

            String comentario = faker.lorem().sentence(10);
            Long clienteId = faker.number().numberBetween(1L, 20L);
            Long productoId = faker.number().numberBetween(1L, 20L);
            Integer rating = faker.number().numberBetween(1, 6);

            // Crea un nuevo comentario
            Comentario comentarioNuevo = new Comentario();
            comentarioNuevo.setComentario(comentario);
            comentarioNuevo.setClienteid(clienteId);
            comentarioNuevo.setProductoid(productoId);
            comentarioNuevo.setRating(rating);

            // Guarda el comentario en la base de datos
            repo.save(comentarioNuevo);
        }

        System.out.println("=== Comentarios de prueba cargados ===");
        repo.findAll().forEach(System.out::println);
    }
}
