package org.grupo6aos.apigestionclientes.exeception;

import org.grupo6aos.apigestionclientes.model.HttpProblem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpProblem> notFoundHandler(NotFoundException e) {
        var body = new HttpProblem("https://httpstatuses.com/404",
                "NOT FOUND",
                404,
                e.getMessage(),
                "about:blank");

        e.printStackTrace();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type","application/problem+json")
                .body(body);
    }

}
