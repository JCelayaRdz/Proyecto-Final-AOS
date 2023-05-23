package org.grupo6aos.apigestionclientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.grupo6aos.apigestionclientes.dto.ClienteDto;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @NotEmpty
    @Pattern(regexp = "^[XYZ]?\\d{7,8}[A-Z]$",
            message = "El identificador de un cliente debe de ser un DNI o NIE valido")
    private String id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellidos;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private Integer edad;

    @Column(name = "numero_telefono")
    @NotEmpty
    @Pattern(regexp = "^(?:(?:\\+|00)34)?[6-9]\\d{8}$",
            message = "El telefono no es valido")
    private String numeroTelefono;

    @Column(name = "correo_electronico")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "El correo electronico no es valido")
    private String correoElectronico;

    @Embedded
    private Direccion direccion;

    public Cliente() {
    }

    public Cliente(String id, String nombre, String apellidos, String numeroTelefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numeroTelefono = numeroTelefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public ClienteDto toDto() {
        return new ClienteDto (
              this.id,
              this.nombre,
              this.apellidos,
              this.sexo,
              this.edad,
              this.numeroTelefono,
              this.correoElectronico,
              this.direccion
        );
    }

    @Override
    public String toString() {
        return "Cliente = {\n" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", sexo=" + sexo +
                ", edad=" + edad +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", direccion=" + direccion +
                "\n}";
    }
}
