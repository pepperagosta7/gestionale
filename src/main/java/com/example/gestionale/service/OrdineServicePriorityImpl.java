package com.example.gestionale.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.model.StatoOrdine;
import com.example.gestionale.repository.OrdineRepository;

@Service
@Qualifier("ordineServiceScontato")
public class OrdineServicePriorityImpl {
    
    private final OrdineRepository ordineRepository;
    private final PrezzoStrategy prezzoStrategy;

    public OrdineServicePriorityImpl(OrdineRepository ordineRepository, @Qualifier("prezzoScontatoStrategy") PrezzoStrategy prezzoStrategy) {
        this.ordineRepository = ordineRepository;
        this.prezzoStrategy = prezzoStrategy;
    }

    public List<Ordine> getAll() {
        return ordineRepository.findAll();
    }

    public Ordine getOne(Long id) {
        return ordineRepository.findById(id).orElse(null);
    }

    public Ordine addOrdine(Ordine ordine) {
        BigDecimal importoCalcolato = prezzoStrategy.calcolaPrezzo(ordine.getImporto());
        ordine.setImporto(importoCalcolato);
        return ordineRepository.save(ordine);
    }

    public Ordine updateStatoOrdine(Long id, StatoOrdine stato) {
        Ordine ordine = ordineRepository.findById(id).orElse(null);
        if (ordine != null) {
            ordine.setStato(stato);
            return ordineRepository.save(ordine);
        }
        return null;
    }

    public void delete(Long id) {
        ordineRepository.deleteById(id);
    }

    public int countByClienteId(Long id) {
        return ordineRepository.countByClienteId(id);
    }
}