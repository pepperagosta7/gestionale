package com.example.gestionale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.gestionale.model.Cliente;
import com.example.gestionale.repository.ClienteRepository;
import com.example.gestionale.repository.OrdineRepository;

@Service
public class ClienteServiceImpl implements ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final OrdineRepository ordineRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, OrdineRepository ordineRepository) {
        this.clienteRepository = clienteRepository;
        this.ordineRepository = ordineRepository;
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public Cliente addCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente updateCliente(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getId())) {
            return clienteRepository.save(cliente);
        } else {
            throw new IllegalArgumentException("Cliente not found with id: " + cliente.getId());
        }
    }

    public void deleteCliente(Long id) {
        if (ordineRepository.countByClienteId(id) == 0) {
            clienteRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cliente with id: " + id + " has existing orders and cannot be deleted.");
        }
    }
}