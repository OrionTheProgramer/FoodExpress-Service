package com.servicios.FoodExpress.config;

import com.servicios.FoodExpress.Category.ProductoCategory;
import com.servicios.FoodExpress.model.Producto;
import com.servicios.FoodExpress.repository.ProductoRepository;
import net.datafaker.Faker;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

/**
 * Carga datos de prueba al iniciar la aplicación.
 * Solo se activa en el perfil 'dev'.
 */

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductoRepository repo;


    @Override
    public void run(String... args) throws Exception {

        // Generador de datos falsos
        Faker faker = new Faker();

        // Borra todo
        repo.deleteAll();

        // Crea productos de prueba
        for (int i = 0; i <= 20; i++) {
            Producto producto = new Producto();
            producto.setNombre(faker.food().dish());
            producto.setDescription(faker.lorem().sentence());

            // Transformando el integer a big integer
            BigInteger precio = BigInteger.valueOf(faker.number().numberBetween(1000, 100000));
            producto.setPrecio(precio);

            // Asignando una categoría aleatoria según ProductoCategory
            List<ProductoCategory> categorias = List.of(ProductoCategory.values());
            producto.setCategoria(categorias.get(faker.number().numberBetween(0, categorias.size())));
            producto.setStock(faker.number().numberBetween(1, 100));
            // Guarda el producto
            repo.save(producto);
        }

        System.out.println("=== Productos de prueba cargados ===");
        repo.findAll().forEach(System.out::println);
    }
}
