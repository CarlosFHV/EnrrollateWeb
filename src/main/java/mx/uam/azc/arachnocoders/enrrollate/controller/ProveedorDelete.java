package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class ProveedorDelete
 */
@WebServlet("/ProveedorDelete")
public class ProveedorDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProveedorDelete() {
      
    }
    protected void doGet( HttpServletRequest request,
    	      HttpServletResponse response ) throws ServletException, IOException
    	  {
    	    // Obtener el parámetro "id_usuario"
    	    String idProveedor = request.getParameter( "id_proveedor");

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
    	     borrarProveedor(key,response); 
    	     String base = request.getContextPath();
    	     response.sendRedirect(base + "/ProveedorUpdateForm");
    	     
    	    }
    	    catch ( Exception e )
    	    {
    	      throw new ServletException( e );
    	    }
    	  }
      
      private void borrarProveedor( int key,
    	      HttpServletResponse response )
    	      throws NamingException, SQLException, IOException
    	  {
    	    Context context = new InitialContext();
    	    DataSource source = ( DataSource )context
    	        .lookup( "java:comp/env/jdbc/TestDS" );

    	    try (Connection connection = source.getConnection())
    	    {
    	      borrarProveedor( connection, key, response );
    	    }
    	  }

      
      private void borrarProveedor(Connection connection, int key, HttpServletResponse response) throws SQLException, IOException {
          String query = "DELETE FROM proveedores WHERE ID_Proveedor = ?";
          try (PreparedStatement statement = connection.prepareStatement(query)) {
              statement.setInt(1, key);
              int rowsAffected = statement.executeUpdate();
          }
      }
}
