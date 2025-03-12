package com.example.gestionale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestionale.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
