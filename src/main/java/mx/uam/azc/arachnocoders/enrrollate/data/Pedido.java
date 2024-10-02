package mx.uam.azc.arachnocoders.enrrollate.data;

import java.sql.Timestamp;

public class Pedido {
    private int idPedido;
    private int idUsuario;
    private Timestamp fechaPedido;
    private String estado;
    private String direccionEnvio;

    // Getters y setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdUsuario() { 
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) { 
        this.idUsuario = idUsuario;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
}
