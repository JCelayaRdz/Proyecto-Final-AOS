package org.grupo6aos.apigestionclientes.service;

import org.grupo6aos.apigestionclientes.dto.ClienteDto;
import org.grupo6aos.apigestionclientes.dto.Link;
import org.grupo6aos.apigestionclientes.exeception.BadRequestException;
import org.grupo6aos.apigestionclientes.exeception.NotFoundException;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    public List<ClienteDto> findAll(Integer page,
                                    String order,
                                    Sort.Direction ordering,
                                    String url) {
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

        var c =  slice.stream()
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

        return c;
    }

    public Map<String, Object> addlinks(ClienteDto cliente, String url) {
        var links = new HashMap<String, Object>();
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

    private boolean shouldReturnFirstPage(Integer page) {
        return page == null || page <= 0;
    }

    private boolean shouldOrderById(String order) {
        return order == null || order.trim().length() == 0;
    }

    private boolean shouldOrderAsc(Sort.Direction ordering) {
        return ordering == null || ordering.isAscending();
    }

    public ClienteDto findOne(String clienteId) {
        return repository.findById(clienteId)
                .map(Cliente::toDto)
                .map(dto -> {
                    dto.setVehiculos(vinService.generateVins());
                    return dto;
                })
                .orElseThrow(NotFoundException::new);
    }

    public ClienteDto saveOne(ClienteDto clienteDto) {
        if (repository.existsById(clienteDto.getId())) throw new BadRequestException();
        var cliente = repository.save(clienteDto.toEntity())
                .toDto();
        cliente.setVehiculos(vinService.generateVins());
        return cliente;
    }

    public ClienteDto updateOne(String clienteId, ClienteDto clienteDto) {
        if (!repository.existsById(clienteId)) throw new NotFoundException();
        return repository.save(clienteDto.toEntity())
                .toDto();
    }

    public void deleteOne(String clienteId) {
        if (!repository.existsById(clienteId)) throw new NotFoundException();
        repository.deleteById(clienteId);
    }
}
