package org.grupo6aos.apigestionclientes.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.grupo6aos.apigestionclientes.dto.ClienteDto;
import org.grupo6aos.apigestionclientes.service.ClienteService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ClienteDto>> findAll(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "ordering", required = false) Sort.Direction ordering,
            HttpServletRequest request
    ) {
        var clientes =  service.findAll(
                page,
                order,
                ordering,
                String.valueOf(request.getRequestURL())
        );

        String etag = Integer.toString(clientes.hashCode());
        return ResponseEntity.ok()
                .header("ETag", etag)
                .body(clientes);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteDto> findOne(@PathVariable String clienteId) {
        var cliente = service.findOne(clienteId);
        String etag = Integer.toString(cliente.hashCode());
        return ResponseEntity.ok()
                .header("ETag", etag)
                .body(cliente);
    }

    @PostMapping
    public ResponseEntity<ClienteDto> saveOne(@Valid @RequestBody ClienteDto cliente,
                                           HttpServletRequest request) {
        var url = request.getRequestURL();
        var clienteGuardado = service.saveOne(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", url+"/"+clienteGuardado.getId())
                .body(clienteGuardado);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<?> updateOne(@PathVariable String clienteId,
                                       @Valid @RequestBody ClienteDto cliente) {
        var clienteActualizado = service.updateOne(clienteId, cliente);
        return ResponseEntity.status(209)
                .body(clienteActualizado);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable String clienteId) {
        service.deleteOne(clienteId);
    }
}
