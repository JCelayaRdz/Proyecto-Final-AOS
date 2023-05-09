package org.grupo6aos.apigestionclientes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService service;

    private final Logger log = LoggerFactory.getLogger(ClienteController.class);

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> findAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "ordering", required = false) Sort.Direction ordering,
            HttpServletRequest request
    ) {
        var clientes =  service.findAll(page, order, ordering)
                .stream()
                .toList();
        String etag = Integer.toString(clientes.hashCode());
        return ResponseEntity.ok()
                .header("ETag", etag)
                .body(clientes);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> findOne(@PathVariable String clienteId) {
        var cliente = service.findOne(clienteId);
        String etag = Integer.toString(cliente.hashCode());
        return ResponseEntity.ok()
                .header("ETag", etag)
                .body(cliente);
    }
}
