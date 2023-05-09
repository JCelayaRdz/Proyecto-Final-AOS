package org.grupo6aos.apigestionclientes.exeception;

public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super("Existe un cliente con el id proporcionado.");
    }
}
