package org.grupo6aos.apigestionclientes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.grupo6aos.apigestionclientes.model.Cliente;
import org.grupo6aos.apigestionclientes.model.Direccion;
import org.grupo6aos.apigestionclientes.model.Sexo;

import java.util.List;
import java.util.Map;

public class ClienteDto {

    @NotEmpty
    @Pattern(regexp = "^[XYZ]?\\d{7,8}[A-Z]$",
            message = "El identificador de un cliente debe de ser un DNI o NIE valido")
    private String id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellidos;

    private Sexo sexo;

    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    private Integer edad;

    @NotEmpty
    @Pattern(regexp = "^(?:(?:\\+|00)34)?[6-9]\\d{8}$\n",
            message = "El telefono no es valido")
    private String numeroTelefono;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n",
            message = "El correo electronico no es valido")
    private String correoElectronico;

    private Direccion direccion;

    private List<String> vehiculos;

    private Map<String, Link> links;

    public ClienteDto(String id,
                      String nombre,
                      String apellidos,
                      Sexo sexo,
                      Integer edad,
                      String numeroTelefono,
                      String correoElectronico,
                      Direccion direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.sexo = sexo;
        this.edad = edad;
        this.numeroTelefono = numeroTelefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
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

    public List<String> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<String> vehiculos) {
        this.vehiculos = vehiculos;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public Cliente toEntity() {
        var cliente = new Cliente (
                this.id,
                this.nombre,
                this.apellidos,
                this.numeroTelefono
        );
        cliente.setSexo(this.sexo);
        cliente.setEdad(this.edad);
        cliente.setCorreoElectronico(this.correoElectronico);
        cliente.setDireccion(this.direccion);

        return cliente;
    }

    @Override
    public String toString() {
        return "ClienteDto = {" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", sexo=" + sexo +
                ", edad=" + edad +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", direccion=" + direccion +
                ", vehiculos=" + vehiculos +
                ", links=" + links +
                '}';
    }
}
