package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Busca todos los comentarios de un cliente específico
    public List<Comentario> findByClienteid(Long cliente_id);

    // Busca todos los comentarios de un producto específico
    public List<Comentario> findByProductoid(Long producto_id);
}
