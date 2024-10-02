package mx.uam.azc.arachnocoders.enrrollate.data;

public class ProductoDTO {
	  private String idProducto;
	    private String nombre;
	    private String descripcion;
	    private double precio;
	    private int stock;
	    private String urlImagen;
		public String getIdProducto() {
			return idProducto;
		}
		public void setIdProducto(String idProducto) {
			this.idProducto = idProducto;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public double getPrecio() {
			return precio;
		}
		public void setPrecio(double precio) {
			this.precio = precio;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		public String getUrlImagen() {
			return urlImagen;
		}
		public void setUrlImagen(String urlImagen) {
			this.urlImagen = urlImagen;
		}

   
}
