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
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;

/**
 * Servlet implementation class ProveedorHTML
 */
@WebServlet("/ProveedorHTML")
public class ProveedorHTML extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProveedorHTML() {
        super();
       
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProveedorDTO> proveedores = getProveedores(response);
            if (proveedores.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay proveedores disponibles.");
                return;
            }
            documentShow(proveedores, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar envíos: " + e.getMessage(), e);
        }
    }

    private List<ProveedorDTO> getProveedores(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getProveedores(connection);
        }
    }

    private List<ProveedorDTO> getProveedores(Connection connection) throws SQLException {
        String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores ";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet cursor = statement.executeQuery()) {
            List<ProveedorDTO> proveedores = new ArrayList<>();
            while (cursor.next()) {
  	    	  ProveedorDTO proveedor = new ProveedorDTO();
                
  	    	  proveedor.setIdProveedor(cursor.getString(1));
  	    	  proveedor.setNombreProveedor(cursor.getString(2));
  	    	  proveedor.setContacto(cursor.getString(3));
  	    	  proveedor.setTelefono(cursor.getString(4));
  	    	  proveedor.setEmail(cursor.getString(5));
                
  	    	  proveedores.add(proveedor);     
            }
            return proveedores;
        }
    }

    public void documentShow(List<ProveedorDTO> proveedores, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteProveedores.html");
        OutputStream fos = response.getOutputStream();

        // Start building HTML content
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Reporte de Proveedores</title></head><body>");
        html.append("<h1>Reporte de Envíos</h1>");

        // Create table headers
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
        .append("<th>ID Proveedor</th>")
        .append("<th>Nombre Proveedor</th>")
        .append("<th>Contacto</th>")
        .append("<th>Telefono</th>")
        .append("<th>Email</th>")
        
            .append("</tr>");

        // Populate table rows
        for (ProveedorDTO proveedor : proveedores) {
            html.append("<tr>")
                .append("<td>").append(proveedor.getIdProveedor()).append("</td>")
                .append("<td>").append(proveedor.getNombreProveedor()).append("</td>")
                .append("<td>").append(proveedor.getContacto()).append("</td>")
                .append("<td>").append(proveedor.getTelefono()).append("</td>")
                .append("<td>").append(proveedor.getEmail()).append("</td>")
                .append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");

        // Write HTML content to response
        fos.write(html.toString().getBytes());
        fos.flush();
        fos.close();
    }

}
