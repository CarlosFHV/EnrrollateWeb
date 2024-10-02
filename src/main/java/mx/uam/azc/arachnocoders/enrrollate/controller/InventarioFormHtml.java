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
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

/**
 * Servlet implementation class InventarioFormHtml
 */
@WebServlet("/InventarioFormHtml")
public class InventarioFormHtml extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InventarioFormHtml() {
    }

    protected void doGet( HttpServletRequest request,
    	      HttpServletResponse response ) throws ServletException, IOException
    	  {
    	    // Obtener el parámetro "id_usuario"
    	    String idInventario = request.getParameter( "id_inventario" );

    	    // Verificar si el parámetro es nulo o está vacío
    	    if ( idInventario == null || idInventario.trim().isEmpty() )
    	    {
    	      // Manejar el caso donde el parámetro no está presente o está vacío
    	      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
    	          "El parámetro 'id_inventario' es requerido." );
    	      return;
    	    }

    	    int key;
    	    try
    	    {
    	      key = Integer.parseInt( idInventario );
    	    }
    	    catch ( NumberFormatException e )
    	    {
    	      // Manejar el caso donde el parámetro no puede ser convertido a un entero
    	      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
    	          "El parámetro 'id_inventario' no es un número válido." );
    	      return;
    	    }

    	    try
    	    {
    	      java.util.List<InventarioDTO> inventarios = getInventarios( key, response );
    	      request.setAttribute( "inventarios", inventarios );
    	    }
    	    catch ( Exception e )
    	    {
    	      throw new ServletException( e );
    	    }
    	  }

    	  /**
    	   * Obtener los usuarios registrados en la base de datos.
    	   */
    	  private java.util.List<InventarioDTO> getInventarios( int key,
    	      HttpServletResponse response )
    	      throws NamingException, SQLException, IOException
    	  {
    	    Context context = new InitialContext();
    	    DataSource source = ( DataSource )context
    	        .lookup( "java:comp/env/jdbc/TestDS" );

    	    try (Connection connection = source.getConnection())
    	    {
    	      return getInventarios( connection, key, response );
    	    }
    	  }

    	  /**
    	   * Realizar la consulta sobre la base de datos, y regresar la lista de
    	   * usuarios.
    	   */
    	  private java.util.List<InventarioDTO> getInventarios( Connection connection, int key,
    	      HttpServletResponse response ) throws SQLException, IOException
    	  {
    	    String query = "SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario WHERE ID_Inventario = ?";
    	    try (PreparedStatement statement = connection.prepareStatement( query ))
    	    {
    	      statement.setInt( 1, key );
    	      ResultSet cursor = statement.executeQuery();

    	      java.util.List<InventarioDTO> inventarios = new ArrayList<>();

    	      while (cursor.next()) {
                  InventarioDTO inventario = new InventarioDTO();
                  
                  inventario.setIdInventario(cursor.getString(1));
                  inventario.setIdProducto(cursor.getString(2));
                  inventario.setCantidad(cursor.getString(3));
                  inventario.setUbicacion(cursor.getString(4));
    
                  inventarios.add(inventario);
              }
    	      documentShow(inventarios, response, key );
    	      return inventarios;
    	    }
    	  }

    	  public void documentShow(java.util.List<InventarioDTO> inventarios, HttpServletResponse response, int key) {
    	    try {
    	        response.setContentType("text/html");
    	        response.addHeader("Content-Disposition", "attachment;filename=ReporteInventariod" + key + ".html");
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
