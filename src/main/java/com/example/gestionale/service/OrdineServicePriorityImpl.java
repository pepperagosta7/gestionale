package com.example.gestionale.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.model.StatoOrdine;
import com.example.gestionale.repository.OrdineRepository;

import jakarta.transaction.Transactional;

@Service
@Qualifier("ordineServiceScontato")
public class OrdineServicePriorityImpl implements OrdineService {

    private final OrdineRepository ordineRepository;
    private final PrezzoStrategy prezzoStrategy;

    public OrdineServicePriorityImpl(OrdineRepository ordineRepository,
            @Qualifier("prezzoScontatoStrategy") PrezzoStrategy prezzoStrategy) {
        this.ordineRepository = ordineRepository;
        this.prezzoStrategy = prezzoStrategy;
    }

    public List<Ordine> getAll() {
        return ordineRepository.findAll();
    }

    public Optional<Ordine> getOne(Long id) {
        return ordineRepository.findById(id);
    }

    public Ordine addOrdine(Ordine ordine) {
        BigDecimal importoCalcolato = prezzoStrategy.calcolaPrezzo(ordine.getImporto());
        ordine.setImporto(importoCalcolato);
        return ordineRepository.save(ordine);
    }

    @Transactional
    public Optional<Ordine> updateStatoOrdine(Long id, StatoOrdine newStatus) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);
        if (ordineOptional.isPresent()) {
            Ordine ordine = ordineOptional.get();
            ordine.setStato(newStatus);
            return Optional.of(ordineRepository.save(ordine));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        ordineRepository.deleteById(id);
    }

    public int countByClienteId(Long id) {
        return ordineRepository.countByClienteId(id);
    }
}