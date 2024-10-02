package mx.uam.azc.arachnocoders.enrrollate.controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;

/**
 * Servlet implementation class InventarioDelete
 */
@WebServlet("/InventarioDelete")
public class InventarioDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InventarioDelete() {
        super();
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
  	     borrarInventario(key,response); 
  	     String base = request.getContextPath();
  	     response.sendRedirect(base + "/InventarioForm");
  	     
  	    }
  	    catch ( Exception e )
  	    {
  	      throw new ServletException( e );
  	    }
  	  }
    
    private void borrarInventario( int key,
  	      HttpServletResponse response )
  	      throws NamingException, SQLException, IOException
  	  {
  	    Context context = new InitialContext();
  	    DataSource source = ( DataSource )context
  	        .lookup( "java:comp/env/jdbc/TestDS" );

  	    try (Connection connection = source.getConnection())
  	    {
  	      borrarInventario( connection, key, response );
  	    }
  	  }

    
    private void borrarInventario(Connection connection, int key, HttpServletResponse response) throws SQLException, IOException {
        String query = "DELETE FROM inventario WHERE ID_Inventario = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, key);
            int rowsAffected = statement.executeUpdate();
        }
    }

}