package com.example.gestionale.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestionale.model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    int countByClienteId(Long clienteid);
    
}
