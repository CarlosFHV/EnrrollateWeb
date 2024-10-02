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
 * Servlet implementation class ProveedorInsert
 */
@WebServlet("/ProveedorInsert")
public class ProveedorInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProveedorInsert() {
    }

    protected void doPost( HttpServletRequest request,
  	      HttpServletResponse response ) throws ServletException, IOException
  	  {

  	    String nombreProveedor = request.getParameter("nombre_proveedor");
          String contacto = request.getParameter("contacto");
          String telefono = request.getParameter("telefono");
          String email = request.getParameter("email");
  	    
          if(nombreProveedor != null && contacto != null && telefono != null && email != null) {
          	try
      	    {
      	     insertarProveedor(nombreProveedor, contacto, telefono, email,response);
      	     String base = request.getContextPath();
      	     response.sendRedirect(base + "/ProveedorUpdateForm");
      	    }
      	    catch ( Exception e )
      	    {
      	    	
      	    	String base = request.getContextPath();
         	     	response.sendRedirect(base + "/ProvedorUpdateForm");
      	    }
          }
          
  	    
  	  }
    
    private void insertarProveedor( String nombreProveedor, String contacto, String telefono, String email,
  	      HttpServletResponse response )
  	      throws NamingException, SQLException, IOException
  	  {
  	    Context context = new InitialContext();
  	    DataSource source = ( DataSource )context
  	        .lookup( "java:comp/env/jdbc/TestDS" );

  	    try (Connection connection = source.getConnection())
  	    {
  	      insertarProveedor( connection, nombreProveedor, contacto, telefono, email, response );
  	    }
  	  }

    
    private void insertarProveedor(Connection connection, String nombreProveedor, String contacto, String telefono, String email,
  		  HttpServletResponse response) throws SQLException, IOException {
        String query = "SELECT * FROM productos WHERE ID_Producto = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
          	  query = "INSERT INTO proveedores(Nombre_Proveedor, Contacto, Teléfono, Email) VALUES "
          	  		+ "(? , ? , ?, ?)";
          	  PreparedStatement statement2 = null;
          	  try {
    	            statement2 = connection.prepareStatement(query);
    	            
    	            statement2.setString(1, nombreProveedor);
    	            statement2.setString(2, contacto);
    	            statement2.setString(3, telefono);
    	            statement2.setString(4,email);
    	            statement2.executeUpdate();
    	        } finally {
    	            if (statement2 != null) {
    	                statement2.close();
    	            }
    	        }
            
        }
    }

}
