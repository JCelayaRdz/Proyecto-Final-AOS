package org.grupo6aos.apigestionclientes.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Direccion {

    @Pattern(regexp = "^C/ [a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+( [a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+)*$\n",
            message = "La calle proporcionada no cumple con el formato valido")
    private String nombreCalle;

    private int numeroEdificacion;

    private String detalles;

    public Direccion() {
    }

    public Direccion(String nombreCalle, int numeroEdificacion, String detalles) {
        this.nombreCalle = nombreCalle;
        this.numeroEdificacion = numeroEdificacion;
        this.detalles = detalles;
    }

    public String getNombreCalle() {
        return nombreCalle;
    }

    public void setNombreCalle(String nombreCalle) {
        this.nombreCalle = nombreCalle;
    }

    public int getNumeroEdificacion() {
        return numeroEdificacion;
    }

    public void setNumeroEdificacion(int numeroEdificacion) {
        this.numeroEdificacion = numeroEdificacion;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Direccion = {\n" +
                "nombreCalle='" + nombreCalle + '\'' +
                ", numeroEdificacion=" + numeroEdificacion +
                ", detalles='" + detalles + '\'' +
                "\n}";
    }
}
