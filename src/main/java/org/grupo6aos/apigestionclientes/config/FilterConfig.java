package org.grupo6aos.apigestionclientes.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<OptionsFilter> optionsFilterRegistration() {
        var registrarionBean = new FilterRegistrationBean<OptionsFilter>();
        registrarionBean.setFilter(new OptionsFilter());
        registrarionBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrarionBean.addUrlPatterns("/*");
        return registrarionBean;
    }

}
