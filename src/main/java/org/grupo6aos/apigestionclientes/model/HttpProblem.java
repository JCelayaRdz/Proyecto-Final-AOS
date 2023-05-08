package org.grupo6aos.apigestionclientes.model;

public record HttpProblem(String type,
                          String title,
                          Integer status,
                          String detail,
                          String instance) {
}
