package com.example.gestionale.service;

import java.util.List;
import java.util.Optional;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.model.StatoOrdine;

public interface OrdineService {

    List<Ordine> getAll();

    Optional<Ordine> getOne(Long id);

    Ordine addOrdine(Ordine ordine);

    Optional<Ordine> updateStatoOrdine(Long id, StatoOrdine stato);

    void delete(Long id);

    int countByClienteId(Long id);
    
}
