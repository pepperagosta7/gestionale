package com.example.gestionale.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.model.StatoOrdine;
import com.example.gestionale.service.ClienteService;
import com.example.gestionale.service.OrdineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ordini")
public class OrdineControllerAPI {

    private final OrdineService ordineServiceBase;
    private final OrdineService ordineServiceScontato;
    private final ClienteService clienteService;

    public OrdineControllerAPI(
            @Qualifier("ordineServiceBase") OrdineService ordineServiceBase,
            @Qualifier("ordineServiceScontato") OrdineService ordineServiceScontato,
            ClienteService clienteService) {
        this.ordineServiceBase = ordineServiceBase;
        this.ordineServiceScontato = ordineServiceScontato;
        this.clienteService = clienteService;
    }

    private OrdineService getOrdineService(Long clienteId) {
        int numeroOrdini = ordineServiceBase.countByClienteId(clienteId);
        return numeroOrdini > 5 ? ordineServiceScontato : ordineServiceBase;
    }

    @GetMapping
    public List<Ordine> getAllOrdini() {
        return ordineServiceBase.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Ordine> getOrdineById(@PathVariable Long id) {
        return ordineServiceBase.getOne(id);
    }

    @PostMapping
    public Ordine addOrdine(@Valid @RequestBody Ordine ordine) {
        OrdineService ordineService = getOrdineService(ordine.getCliente().getId());
        return ordineService.addOrdine(ordine);
    }

    @PutMapping("/{id}/stato")
    public Optional<Ordine> updateStatoOrdine(@PathVariable Long id, @Valid @RequestBody StatoOrdine stato) {
        return ordineServiceBase.updateStatoOrdine(id, stato);
    }

    @DeleteMapping("/{id}")
    public void deleteOrdine(@PathVariable Long id) {
        ordineServiceBase.delete(id);
    }
}