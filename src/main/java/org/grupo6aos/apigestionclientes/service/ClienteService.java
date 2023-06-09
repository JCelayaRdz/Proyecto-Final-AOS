package org.grupo6aos.apigestionclientes.service;

import org.grupo6aos.apigestionclientes.dto.ClienteDto;
import org.grupo6aos.apigestionclientes.dto.Link;
import org.grupo6aos.apigestionclientes.exeception.BadRequestException;
import org.grupo6aos.apigestionclientes.exeception.NotFoundException;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    private final VinService vinService;

    private final static int PAGE_SIZE = 5;

    private final static String VEHICULOS_URL = "https://example.com/api/v1/vehiculos";

    private final Logger log = LoggerFactory.getLogger(ClienteService.class);

    public ClienteService(ClienteRepository repository,
                          VinService vinService) {
        this.repository = repository;
        this.vinService = vinService;
    }

    public Map<String, Object> findAll(Integer page,
                                    String order,
                                    Sort.Direction ordering,
                                    String url) {
        var requestPayload = new LinkedHashMap<String, Object>();
        var pageNumber = 0;
        var orderBy = "id";
        var direction = Sort.Direction.ASC;

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

        var clientes =  slice.stream()
                .map(Cliente::toDto)
                .map(dto -> {
                    dto.setVehiculos(vinService.generateVins());
                    return dto;
                })
                .map(dto -> {
                    dto.setLinks(addlinks(dto, url));
                    return dto;
                })
                .toList();

        requestPayload.put("clientes", clientes);
        requestPayload.put("links", getPageLinks(slice, url));

        return requestPayload;
    }

    public Map<String, Object> addlinks(ClienteDto cliente, String url) {
        var links = new LinkedHashMap<String, Object>();
        var vehiculos = new ArrayList<Link>();
        links.put("parent", new Link (
                url,
                "cliente_cget cliente_post"
        ));

        links.put("self", new Link (
           url+"/"+cliente.getId(),
                "cliente_get cliente_delete cliente_put"
        ));

        cliente.getVehiculos()
                .forEach(vin -> {
                    vehiculos.add(new Link(
                            VEHICULOS_URL+"/"+vin,
                            "vehiculo_get vehiculo_delete vehiculo_put"
                    ));
                });

        links.put("vehiculos", vehiculos);

        return links;
    }

    public Map<String, Link> getPageLinks(Page<Cliente> page, String url) {
        var links = new LinkedHashMap<String, Link>();
        var currentPageNumber = page.getNumber();
        var previousPageNumber = currentPageNumber - 1;
        var nextPageNumber = currentPageNumber + 1;

        if (previousPageNumber >= 0) {
            links.put("prevPage",new Link(url+"?page="+previousPageNumber, "prevPage"));
        }
        if (nextPageNumber <= page.getTotalPages()) {
            links.put("nextPage", new Link(url+"?page="+nextPageNumber, "nextPage"));
        }

        return links;
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

    public ClienteDto findOne(String clienteId, String url) {
        return repository.findById(clienteId)
                .map(Cliente::toDto)
                .map(dto -> {
                    dto.setVehiculos(vinService.generateVins());
                    return dto;
                })
                .map(dto -> {
                    dto.setLinks(addlinks(dto, url));
                    return dto;
                })
                .orElseThrow(NotFoundException::new);

    }

    public ClienteDto saveOne(ClienteDto clienteDto, String url) {
        if (repository.existsById(clienteDto.getId())) throw new BadRequestException();
        var cliente = repository.save(clienteDto.toEntity())
                .toDto();
        cliente.setVehiculos(vinService.generateVins());
        cliente.setLinks(addlinks(cliente, url));
        return cliente;
    }

    public ClienteDto updateOne(String clienteId, ClienteDto clienteDto, String url) {
        if (!repository.existsById(clienteId)) throw new NotFoundException();
        var clienteGuardado = repository.findById(clienteId).get();

        // Si alguno de los campos no obligatorios es null, establecemos estos como los del cliente que esta en la bd
        if (clienteDto.getSexo() == null) {
            clienteDto.setSexo(clienteGuardado.getSexo());
        }
        if (clienteDto.getEdad() == null) {
            clienteDto.setEdad(clienteGuardado.getEdad());
        }
        if (clienteDto.getCorreoElectronico() == null) {
            clienteDto.setCorreoElectronico(clienteGuardado.getCorreoElectronico());
        }
        if (clienteDto.getDireccion().getDetalles() == null) {
            clienteDto.getDireccion().setDetalles(clienteGuardado.getDireccion().getDetalles());
        }

        var cliente =  repository.save(clienteDto.toEntity())
                .toDto();
        cliente.setVehiculos(vinService.generateVins());
        cliente.setLinks(addlinks(cliente, url));
        return cliente;
    }

    public void deleteOne(String clienteId) {
        if (!repository.existsById(clienteId)) throw new NotFoundException();
        repository.deleteById(clienteId);
    }
}
