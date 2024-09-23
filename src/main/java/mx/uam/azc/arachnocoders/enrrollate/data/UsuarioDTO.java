/**
 * 
 */
package mx.uam.azc.arachnocoders.enrrollate.data;

import java.io.Serializable;

/**
 * 
 */
public class UsuarioDTO implements Serializable {
    private String _idUsuario;
    private String _nombre;
    /**
     * @return the idUsuario
     */
    public String getIdUsuario() {
        return _idUsuario;
    }
    /**
     * @param idUsuario the idUsuario to set
     */
    public void setIdUsuario(String idUsuario) {
        _idUsuario = idUsuario;
    }
    /**
     * @return the nombre
     */
    public String getNombre() {
        return _nombre;
    }
    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        _nombre = nombre;
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return _email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        _email = email;
    }
    /**
     * @return the direccion
     */
    public String getDireccion() {
        return _direccion;
    }
    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        _direccion = direccion;
    }
    /**
     * @return the telefono
     */
    public String getTelefono() {
        return _telefono;
    }
    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        _telefono = telefono;
    }
    /**
     * @return the rol
     */
    public String getRol() {
        return _rol;
    }
    /**
     * @param rol the rol to set
     */
    public void setRol(String rol) {
        _rol = rol;
    }
    /**
     * @return the fechaRegistro
     */
    public String getFechaRegistro() {
        return _fechaRegistro;
    }
    /**
     * @param fechaRegistro the fechaRegistro to set
     */
    public void setFechaRegistro(String fechaRegistro) {
        _fechaRegistro = fechaRegistro;
    }
    private String _email;
    private String _direccion;
    private String _telefono;
    private String _rol;
    private String _fechaRegistro;
 
}
