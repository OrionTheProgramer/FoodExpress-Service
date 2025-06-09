package com.servicios.FoodExpress.repository;

import com.servicios.FoodExpress.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    public List<Cliente> findByCategoria(String categoria);

}
