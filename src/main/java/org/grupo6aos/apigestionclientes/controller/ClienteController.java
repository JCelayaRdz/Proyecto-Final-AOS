package org.grupo6aos.apigestionclientes.controller;

import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> findAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "ordering", required = false) Sort.Direction ordering
    ) {
        return service.findAll(page, order, ordering)
                .stream()
                .toList();
    }

    @GetMapping("/{clienteId}")
    public Cliente findOne(@PathVariable String clienteId) {
        return service.findOne(clienteId);
    }
}
