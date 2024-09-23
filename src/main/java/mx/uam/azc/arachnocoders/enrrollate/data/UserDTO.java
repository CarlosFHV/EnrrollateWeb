package mx.uam.azc.arachnocoders.enrrollate.data;

import java.io.Serializable;

/**
 * DTO para representar un usuario.
 */
public class UserDTO implements Serializable {
    private int _id;
    private String _nombre;
    private String _email;
    private String _direccion;
    private String _telefono;
    private String _rol;
    private String _fechaRegistro;

    // Getter y Setter para ID
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        _id = id;
    }

    // Getter y Setter para Nombre
    public String getNombre() {
        return _nombre;
    }
    public void setNombre(String nombre) {
        _nombre = nombre;
    }

    // Getter y Setter para Email
    public String getEmail() {
        return _email;
    }
    public void setEmail(String email) {
        _email = email;
    }

    // Getter y Setter para Dirección
    public String getDireccion() {
        return _direccion;
    }
    public void setDireccion(String direccion) {
        _direccion = direccion;
    }

    // Getter y Setter para Teléfono
    public String getTelefono() {
        return _telefono;
    }
    public void setTelefono(String telefono) {
        _telefono = telefono;
    }

    // Getter y Setter para Rol
    public String getRol() {
        return _rol;
    }
    public void setRol(String rol) {
        _rol = rol;
    }

    // Getter y Setter para Fecha de Registro
    public String getFechaRegistro() {
        return _fechaRegistro;
    }
    public void setFechaRegistro(String fechaRegistro) {
        _fechaRegistro = fechaRegistro;
    }
}
