package org.grupo6aos.apigestionclientes.repository;

import org.grupo6aos.apigestionclientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}
