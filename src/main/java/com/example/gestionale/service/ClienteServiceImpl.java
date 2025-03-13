package com.example.gestionale.service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente addCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> updateCliente(Long id, Cliente clienteUp) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (existingCliente.isPresent()) {
            Cliente cliente = existingCliente.get();
            cliente.setNome(clienteUp.getNome());
            cliente.setEmail(clienteUp.getEmail());
            cliente.setTelefono(clienteUp.getTelefono());
            return Optional.of(clienteRepository.save(cliente));
        }
        return Optional.empty();
    }


    public void deleteCliente(Long id) {
        if (ordineRepository.countByClienteId(id) == 0) {
            clienteRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Cliente with id: " + id + " has existing orders and cannot be deleted.");
        }
    }

}