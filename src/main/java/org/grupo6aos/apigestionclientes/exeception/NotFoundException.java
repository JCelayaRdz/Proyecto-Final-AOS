package org.grupo6aos.apigestionclientes.exeception;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super("El recurso solicitado no existe.");
    }

}
