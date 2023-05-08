package org.grupo6aos.apigestionclientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Embeddable
public class Direccion {

    @Column(name = "nombre_calle")
    @NotEmpty
    @Pattern(regexp = "^C/ [a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+( [a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+)*$\n",
            message = "La calle proporcionada no cumple con el formato valido")
    private String nombreCalle;

    @Column(name = "numero_edificacion")
    @NotNull
    private Integer numeroEdificacion;

    private String detalles;

    public Direccion() {
    }

    public Direccion(String nombreCalle, Integer numeroEdificacion, String detalles) {
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

    public Integer getNumeroEdificacion() {
        return numeroEdificacion;
    }

    public void setNumeroEdificacion(Integer numeroEdificacion) {
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
