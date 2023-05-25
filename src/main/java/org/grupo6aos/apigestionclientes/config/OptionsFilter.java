package org.grupo6aos.apigestionclientes.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OptionsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        var response = (HttpServletResponse) res;
        var request = (HttpServletRequest) req;
        var url = request.getRequestURL().toString();
        var OPTIONS = HttpMethod.OPTIONS.toString();

        if (request.getMethod().equals(OPTIONS)) {
            if (url.endsWith("clientes")){
                response.setHeader("Allow", "GET, POST, OPTIONS");
            } else {
                response.setHeader("Allow", "GET, PUT, DELETE, OPTIONS");
            }
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            response.setHeader("Access-Control-Expose-Headers", "Allow");
        } else {
            chain.doFilter(req, res);
        }

    }
}
