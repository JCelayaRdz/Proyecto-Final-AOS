package org.grupo6aos.apigestionclientes.exeception;

import org.grupo6aos.apigestionclientes.model.HttpProblem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadRequestAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<HttpProblem> notFoundHandler(BadRequestException e) {
        var body = new HttpProblem("https://httpstatuses.com/400",
                "BAD REQUEST",
                400,
                e.getMessage(),
                "about:blank");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type","application/problem+json")
                .body(body);
    }
}
