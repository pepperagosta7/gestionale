package com.example.gestionale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.gestionale.model.Cliente;
import com.example.gestionale.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clienti")
public class ClienteControllerAPI {

    private final ClienteService clienteService;

    public ClienteControllerAPI(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> getAllClienti() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteService.findById(id).orElse(null);
    }

    @PostMapping
    public Cliente addCliente(@Valid @RequestBody Cliente cliente) {
        return clienteService.addCliente(cliente);
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return clienteService.updateCliente(id, cliente).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable Long id) {
        clienteService.deleteCliente(id);
    }
}