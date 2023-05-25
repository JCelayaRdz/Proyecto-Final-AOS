package org.grupo6aos.apigestionclientes.exeception;

import org.grupo6aos.apigestionclientes.model.HttpProblem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UnprocessableEntityAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpProblem> unprocessableEntityHandler(MethodArgumentNotValidException e) {
        var body = new HttpProblem(
                "https://httpstatuses.com/422",
                "UNPROCESSABLE ENTITY",
                422,
                "Faltan atributos obligatorios",
                "about:blank"
        );

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .header("Content-Type","application/problem+json")
                .body(body);
    }
}
