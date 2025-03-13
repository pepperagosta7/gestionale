package com.example.gestionale.service;

import java.util.List;
import java.util.Optional;

import com.example.gestionale.model.Cliente;

public interface ClienteService {

    List<Cliente> findAll();

    Optional<Cliente> findById(Long id);

    Cliente addCliente(Cliente cliente);

    Optional<Cliente> updateCliente(Long id, Cliente cliente);
    
    void deleteCliente(Long id);
    
}
