package org.grupo6aos.apigestionclientes.service;

import org.grupo6aos.apigestionclientes.exeception.NotFoundException;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.repository.ClienteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Slice<Cliente> findAll(Integer page, String order, Sort.Direction ordering) {
        if (ordering != Sort.Direction.DESC) {
            return repository.findAll(PageRequest.of(page, 5, Sort.by(order).ascending()));
        }
        return repository.findAll(PageRequest.of(page, 5, Sort.by(order).descending()));
    }

    public Cliente findOne(String clienteId) {
        return repository.findById(clienteId)
                .orElseThrow(NotFoundException::new);
    }
}
