package mx.uam.azc.arachnocoders.enrrollate.data;

public class CarritoDTO {
    private int _idProducto;
    private String _nombre;
    private int _cantidad;
	public int getIdProducto() {
		return _idProducto;
	}

	
	public String getNombre() {
		return _nombre;
	}


	public void setNombre(String nombre) {
		_nombre = nombre;
	}


	public int getCantidad() {
		return _cantidad;
	}


	public void setCantidad(int cantidad) {
		_cantidad = cantidad;
	}


	public void setIdProducto(int idProducto) {
		_idProducto = idProducto;
	}


	public CarritoDTO(int idProducto, String nombre, int cantidad) {
		_idProducto = idProducto;
		_nombre = nombre;
		_cantidad = cantidad;
	}


}
