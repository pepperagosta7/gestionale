package com.example.gestionale.service;

import java.util.List;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.model.StatoOrdine;

public interface OrdineService {

    List<Ordine> getAll();

    Ordine getOne(Long id);

    Ordine addOrdine(Ordine ordine);

    Ordine updateStatoOrdine(Long id, StatoOrdine stato);

    void delete(Long id);

    int countByClienteId(Long id);
    
}
