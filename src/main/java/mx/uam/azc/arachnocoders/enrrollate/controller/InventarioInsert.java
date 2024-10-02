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

/**
 * Servlet implementation class InventarioInsert
 */
@WebServlet("/InventarioInsert")
public class InventarioInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InventarioInsert() {

    }

    protected void doPost( HttpServletRequest request,
    	      HttpServletResponse response ) throws ServletException, IOException
    	  {
 
    	    String idProducto = request.getParameter("ID_Producto");
            String cantidadDisponible = request.getParameter("Cantidad_Disponible");
            String ubicacion = request.getParameter("Ubicacion");
    	    
            if(idProducto != null && cantidadDisponible != null && ubicacion != null) {
            	try
        	    {
        	     insertarInventario(idProducto, cantidadDisponible, ubicacion,response);
        	     String base = request.getContextPath();
        	     response.sendRedirect(base + "/InventarioForm");
        	    }
        	    catch ( Exception e )
        	    {
        	    	
        	    	String base = request.getContextPath();
           	     	response.sendRedirect(base + "/InventarioForm");
        	    }
            }
            
    	    
    	  }
      
      private void insertarInventario( String idProducto, String cantidad, String ubicacion,
    	      HttpServletResponse response )
    	      throws NamingException, SQLException, IOException
    	  {
    	    Context context = new InitialContext();
    	    DataSource source = ( DataSource )context
    	        .lookup( "java:comp/env/jdbc/TestDS" );

    	    try (Connection connection = source.getConnection())
    	    {
    	      insertarInventario( connection, idProducto, cantidad, ubicacion, response );
    	    }
    	  }

      
      private void insertarInventario(Connection connection, String idProducto, String cantidad, String ubicacion,
    		  HttpServletResponse response) throws SQLException, IOException {
          String query = "SELECT * FROM productos WHERE ID_Producto = ?";
          try (PreparedStatement statement = connection.prepareStatement(query)) {
              statement.setString(1, idProducto);
              int rowsAffected = statement.executeUpdate();
              if(rowsAffected > 0) {
            	  query = "INSERT INTO inventario(ID_Producto, Cantidad_Disponible,Ubicación_Almacen) VALUES "
            	  		+ "(? , ? , ?)";
            	  PreparedStatement statement2 = null;
            	  try {
      	            statement2 = connection.prepareStatement(query);
      	            
      	            statement2.setString(1, idProducto);
      	            statement2.setString(2, cantidad);
      	            statement2.setString(3, ubicacion);
      	            statement2.executeUpdate();
      	        } finally {
      	            if (statement2 != null) {
      	                statement2.close();
      	            }
      	        }
              }
          }
      }

}