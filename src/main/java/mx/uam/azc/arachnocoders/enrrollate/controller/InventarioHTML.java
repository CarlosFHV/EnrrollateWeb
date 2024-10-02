package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 
import java.util.List; 

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;


/**
 * Servlet implementation class ProveedorHTML
 */
@WebServlet("/InventarioHTML")
public class InventarioHTML extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InventarioHTML() {
        super();
       
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<InventarioDTO> inventarios = getInventarios(response);
            if (inventarios.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay proveedores disponibles.");
                return;
            }
            documentShow(inventarios, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar inventarios: " + e.getMessage(), e);
        }
    }

    private List<InventarioDTO> getInventarios(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getInventarios(connection);
        }
    }

    private List<InventarioDTO> getInventarios(Connection connection) throws SQLException {
        String query = "SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario ";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet cursor = statement.executeQuery()) {
            List<InventarioDTO> inventarios = new ArrayList<>();
            while (cursor.next()) {
                InventarioDTO inventario = new InventarioDTO();
                
                inventario.setIdInventario(cursor.getString(1));
                inventario.setIdProducto(cursor.getString(2));
                inventario.setCantidad(cursor.getString(3));
                inventario.setUbicacion(cursor.getString(4));
  
                inventarios.add(inventario);
            }
            return inventarios;
        }
    }

    public void documentShow(java.util.List<InventarioDTO> inventarios, HttpServletResponse response) {
	    try {
	        response.setContentType("text/html");
	        response.addHeader("Content-Disposition", "attachment;filename=ReporteInventariod" + ".html");
	        OutputStream fos = response.getOutputStream();

	        // Start building HTML content
	        StringBuilder html = new StringBuilder();
	        html.append("<html><head><title>Detalles del Inventario</title></head><body>");
	        html.append("<h1>Detalles del Inventario</h1>");
	        
	        // Create table headers
	        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
	        html.append("<tr>")
	            .append("<th>ID Inventario</th>")
	            .append("<th>ID Producto</th>")
	            .append("<th>Cantidad Disponible</th>")
	            .append("<th>Ubicación de Almacen</th>")
	            
	            .append("</tr>");

	        // Populate table rows
	        for (InventarioDTO inventario : inventarios) {
	            html.append("<tr>")
	                .append("<td>").append(inventario.getIdInventario()).append("</td>")
	                .append("<td>").append(inventario.getIdProducto()).append("</td>")
	                .append("<td>").append(inventario.getCantidad()).append("</td>")
	                .append("<td>").append(inventario.getUbicacion()).append("</td>")
	              
	                .append("</tr>");
	        }
	        html.append("</table>");
	        html.append("</body></html>");

	        // Write HTML content to response
	        fos.write(html.toString().getBytes());
	        fos.flush();
	        fos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
