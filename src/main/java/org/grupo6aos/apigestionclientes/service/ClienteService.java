package org.grupo6aos.apigestionclientes.service;

import org.grupo6aos.apigestionclientes.exeception.NotFoundException;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> findAll() {
        var clientes = repository.findAll();
        if (clientes.isEmpty()) {
            throw new NotFoundException();
        }
        return clientes;
    }
}
