package com.example.gestionale.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.gestionale.model.Ordine;
import com.example.gestionale.service.ClienteService;
import com.example.gestionale.service.OrdineService;

@Controller
@RequestMapping("/ordini")
public class OrdineControllerThymeLeaf {

    private final OrdineService ordineServiceBase;
    private final OrdineService ordineServiceScontato;
    private final ClienteService clienteService;

    public OrdineControllerThymeLeaf(
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
    public String getAllOrdini(Model model) {
        List<Ordine> ordini = ordineServiceBase.getAll();
        model.addAttribute("ordini", ordini);
        return "ordini/list";
    }

    @GetMapping("/{id}")
    public String getOrdineById(@PathVariable Long id, Model model) {
        Optional<Ordine> ordine = ordineServiceBase.getOne(id);
        if (ordine.isPresent()) {
            model.addAttribute("ordine", ordine.get());
            return "ordini/view";
        } else {
            return "ordini/not-found";
        }
    }

    @GetMapping("/new")
    public String showAddOrdineForm(Model model) {
        model.addAttribute("ordine", new Ordine());
        model.addAttribute("clienti", clienteService.findAll());
        return "ordini/form";
    }

    @PostMapping
    public String addOrdine(@ModelAttribute Ordine ordine) {
        OrdineService ordineService = getOrdineService(ordine.getCliente().getId());
        ordineService.addOrdine(ordine);
        return "redirect:/ordini";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateOrdineForm(@PathVariable Long id, Model model) {
        Optional<Ordine> ordine = ordineServiceBase.getOne(id);
        if (ordine.isPresent()) {
            model.addAttribute("ordine", ordine.get());
            model.addAttribute("clienti", clienteService.findAll());
            return "ordini/form";
        } else {
            return "ordini/not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateOrdine(@PathVariable Long id, @ModelAttribute Ordine ordine) {
        ordine.setId(id);
        ordineServiceBase.updateStatoOrdine(id, ordine.getStato());
        return "redirect:/ordini";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrdine(@PathVariable Long id) {
        ordineServiceBase.delete(id);
        return "redirect:/ordini";
    }
}