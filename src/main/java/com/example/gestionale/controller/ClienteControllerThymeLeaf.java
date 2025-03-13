package com.example.gestionale.controller;

import com.example.gestionale.model.Cliente;
import com.example.gestionale.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clienti")
public class ClienteControllerThymeLeaf {

    private final ClienteService clienteService;

    public ClienteControllerThymeLeaf(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping()
    public String getAllClients(Model model) {
        model.addAttribute("clienti", clienteService.findAll());
        return "clienti"; 
    }

    @GetMapping("/modifica/{id}")
    public String editClient(@PathVariable("id") Long id, Model model) {
        clienteService.findById(id).ifPresent(cliente -> model.addAttribute("cliente", cliente));
        return "modifica_cliente"; 
    }

    @PostMapping("/modifica/{id}")
    public String updateCliente(@PathVariable("id") Long id, @ModelAttribute Cliente cliente) {
        clienteService.updateCliente(id, cliente);
        return "redirect:/clienti"; 
    }

    @GetMapping("/elimina/{id}")
    public String deleteCliente(@PathVariable("id") Long id) {
        clienteService.deleteCliente(id);
        return "redirect:/clienti"; 
    }

    @PostMapping("/nuovo")
    public String addOrder(@ModelAttribute Cliente cliente) {
        clienteService.addCliente(cliente);
        return "redirect:/clienti";
    }
}