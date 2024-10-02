package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

/**
 * Servlet implementation class InventarioFormHtml
 */
@WebServlet("/ProveedorFormHtml")
public class ProveedorFormHtml extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProveedorFormHtml() {
    }

    protected void doGet( HttpServletRequest request,
    	      HttpServletResponse response ) throws ServletException, IOException
    	  {
    	    // Obtener el parámetro "id_usuario"
    	    String idProveedor = request.getParameter( "id_proveedor" );

    	    // Verificar si el parámetro es nulo o está vacío
    	    if ( idProveedor == null || idProveedor.trim().isEmpty() )
    	    {
    	      // Manejar el caso donde el parámetro no está presente o está vacío
    	      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
    	          "El parámetro 'id_proveedor' es requerido." );
    	      return;
    	    }

    	    int key;
    	    try
    	    {
    	      key = Integer.parseInt( idProveedor );
    	    }
    	    catch ( NumberFormatException e )
    	    {
    	      // Manejar el caso donde el parámetro no puede ser convertido a un entero
    	      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
    	          "El parámetro 'id_proveedor' no es un número válido." );
    	      return;
    	    }

    	    try
    	    {
    	      java.util.List<ProveedorDTO> proveedores = getProveedor( key, response );
    	      request.setAttribute( "proveedores", proveedores );
    	    }
    	    catch ( Exception e )
    	    {
    	      throw new ServletException( e );
    	    }
    	  }

    	  /**
    	   * Obtener los usuarios registrados en la base de datos.
    	   */
    	  private java.util.List<ProveedorDTO> getProveedor( int key,
    	      HttpServletResponse response )
    	      throws NamingException, SQLException, IOException
    	  {
    	    Context context = new InitialContext();
    	    DataSource source = ( DataSource )context
    	        .lookup( "java:comp/env/jdbc/TestDS" );

    	    try (Connection connection = source.getConnection())
    	    {
    	      return getProveedor( connection, key, response );
    	    }
    	  }

    	  /**
    	   * Realizar la consulta sobre la base de datos, y regresar la lista de
    	   * usuarios.
    	   */
    	  private java.util.List<ProveedorDTO> getProveedor( Connection connection, int key,
    	      HttpServletResponse response ) throws SQLException, IOException
    	  {
    	    String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores WHERE ID_Proveedor = ?";
    	    try (PreparedStatement statement = connection.prepareStatement( query ))
    	    {
    	      statement.setInt( 1, key );
    	      ResultSet cursor = statement.executeQuery();

    	      java.util.List<ProveedorDTO> proveedores = new ArrayList<>();

    	      while (cursor.next()) {
    	    	  ProveedorDTO proveedor = new ProveedorDTO();
                  
    	    	  proveedor.setIdProveedor(cursor.getString(1));
    	    	  proveedor.setNombreProveedor(cursor.getString(2));
    	    	  proveedor.setContacto(cursor.getString(3));
    	    	  proveedor.setTelefono(cursor.getString(4));
    	    	  proveedor.setEmail(cursor.getString(5));
                  
    	    	  proveedores.add(proveedor);     
              }
    	      documentShow(proveedores, response, key );
    	      return proveedores;
    	    }
    	  }

    	  public void documentShow(java.util.List<ProveedorDTO> proveedores, HttpServletResponse response, int key) {
    	    try {
    	        response.setContentType("text/html");
    	        response.addHeader("Content-Disposition", "attachment;filename=ReporteInventariod" + key + ".html");
    	        OutputStream fos = response.getOutputStream();

    	        // Start building HTML content
    	        StringBuilder html = new StringBuilder();
    	        html.append("<html><head><title>Detalles del Proveedor</title></head><body>");
    	        html.append("<h1>Detalles del Proveedor</h1>");
    	        
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
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
    
}