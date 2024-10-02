package mx.uam.azc.arachnocoders.enrrollate.data;
import java.io.Serializable;

public class InventarioDTO implements Serializable{
private String idInventario;
private String idProducto;
private String cantidad;
private String ubicacion;;
public String getIdInventario() {
	return idInventario;
}
public void setIdInventario(String idInventario) {
	this.idInventario = idInventario;
}
public String getIdProducto() {
	return idProducto;
}
public void setIdProducto(String idProducto) {
	this.idProducto = idProducto;
}
public String getCantidad() {
	return cantidad;
}
public void setCantidad(String cantidad) {
	this.cantidad = cantidad;
}
public String getUbicacion() {
	return ubicacion;
}
public void setUbicacion(String ubicacion) {
	this.ubicacion = ubicacion;
}

}
