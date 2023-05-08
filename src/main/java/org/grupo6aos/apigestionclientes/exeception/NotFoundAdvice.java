package org.grupo6aos.apigestionclientes.exeception;

import org.grupo6aos.apigestionclientes.model.HttpProblem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public HttpProblem notFoundHandler(NotFoundException e) {
        return new HttpProblem("https://httpstatuses.com/404",
                "NOT FOUND",
                404,
                e.getMessage(),
                "about:blank");
    }

}
