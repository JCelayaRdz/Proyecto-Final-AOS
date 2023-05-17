package org.grupo6aos.apigestionclientes.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Clase que actua como mock del servicio de de vehiculos,
genera una lista de VIN's que se añadiran a cada cliente
guardado en la base de datos de nuestro servicio
 */
@Service
public class VinService {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPRSTUVWXYZ23456789";

    public List<String> generateVins() {
        var vins = new ArrayList<String>();
        Random random = new Random();
        int numVin = random.nextInt(3) + 1;

        for (int i = 0; i < numVin; i++) {
            var sb = new StringBuilder();

            for (int j = 0; j < 8; j++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }

            sb.append(random.nextInt(10) + "X");

            for (int j = 0; j < 2; j++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }

            for (int j = 0; j < 6; j++) {
                sb.append(random.nextInt(10));
            }

            vins.add(sb.toString());
        }

        return vins;
    }
}
