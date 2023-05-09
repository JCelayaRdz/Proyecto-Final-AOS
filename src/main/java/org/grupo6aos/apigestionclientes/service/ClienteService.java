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

    private final static int PAGE_SIZE = 5;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Slice<Cliente> findAll(Integer page, String order, Sort.Direction ordering) {
        int pageNumber = 0;
        String orderBy = "id";
        Sort.Direction direction = Sort.Direction.ASC;

        if (!shouldReturnFirstPage(page)) {
            pageNumber = page;
        }
        if (!shouldOrderById(order)) {
            orderBy = order;
        }
        if (!shouldOrderAsc(ordering)) {
            direction = ordering;
        }

        var slice =  repository.findAll(
                PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(direction, orderBy))
        );

        if (slice.getNumberOfElements() == 0) throw new NotFoundException();

        return slice;
    }

    private boolean shouldReturnFirstPage(Integer page) {
        return page == null || page <= 0;
    }

    private boolean shouldOrderById(String order) {
        return order == null || order.trim().length() == 0;
    }

    private boolean shouldOrderAsc(Sort.Direction ordering) {
        return ordering == null || ordering.isAscending();
    }

    public Cliente findOne(String clienteId) {
        return repository.findById(clienteId)
                .orElseThrow(NotFoundException::new);
    }
}
