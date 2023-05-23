package org.grupo6aos.apigestionclientes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/*
Clase que se encarga de insertar los datos del script sample-data.sql
si dichos datos no han sido insertados con anterioridad en la base de datos
 */
@Configuration
public class SampleDataConfig {

    @Bean
    public CommandLineRunner dataInitialization(DataSource dataSource) {
        return args -> {
          var populator = new ResourceDatabasePopulator();
          populator.addScript(new ClassPathResource("sample-data.sql"));
            DatabasePopulatorUtils.execute(populator, dataSource);
        };
    }

}
