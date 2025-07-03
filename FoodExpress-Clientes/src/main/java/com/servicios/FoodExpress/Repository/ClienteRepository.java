package com.servicios.FoodExpress.Repository;

import com.servicios.FoodExpress.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    public List<Cliente> findByCategoria(String categoria);

}
