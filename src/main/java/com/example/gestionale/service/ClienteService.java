package com.example.gestionale.service;

import java.util.List;

import com.example.gestionale.model.Cliente;

public interface ClienteService {

    List<Cliente> findAll();

    Cliente findById(Long id);

    Cliente addCliente(Cliente cliente);

    Cliente updateCliente(Cliente cliente);
    
    void deleteCliente(Long id);
    
}
