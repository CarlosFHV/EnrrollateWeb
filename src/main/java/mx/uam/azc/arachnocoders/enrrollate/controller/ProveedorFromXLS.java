package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class ProveedorFromXLS
 */
@WebServlet("/ProveedorFromXLS")
public class ProveedorFromXLS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProveedorFromXLS() {
        super();
        // TODO Auto-generated constructor stub
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
  	      List<ProveedorDTO> proveedores = getProveedores( key, response );
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
  	  private List<ProveedorDTO> getProveedores( int key, HttpServletResponse response )
  	      throws NamingException, SQLException, IOException
  	  {
  	    Context context = new InitialContext();
  	    DataSource source = ( DataSource )context
  	        .lookup( "java:comp/env/jdbc/TestDS" );

  	    try (Connection connection = source.getConnection())
  	    {
  	      return getProveedores( connection, key, response );
  	    }
  	  }

  	  /**
  	   * Realizar la consulta sobre la base de datos, y regresar la lista de
  	   * usuarios.
  	   */
  	  private List<ProveedorDTO> getProveedores( Connection connection, int key,
  	      HttpServletResponse response ) throws SQLException, IOException
  	  {
  	    String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores WHERE ID_Proveedor = ?";
  	    try (PreparedStatement statement = connection.prepareStatement( query ))
  	    {
  	      statement.setInt( 1, key );
  	      ResultSet cursor = statement.executeQuery();

  	      List<ProveedorDTO> proveedores = new ArrayList<>();
  	      Map<String, ProveedorDTO> beans = new HashMap<>();
  	      while ( cursor.next() )
  	      {
  	    	  ProveedorDTO proveedor = new ProveedorDTO();  
                proveedor.setIdProveedor(cursor.getString(1));
                proveedor.setNombreProveedor(cursor.getString(2));
                proveedor.setContacto(cursor.getString(3));
                proveedor.setTelefono(cursor.getString(4));
                proveedor.setEmail(cursor.getString(5));
                proveedores.add(proveedor);
  	        beans.put( "proveedor", proveedor );
  	      }
  	      xlsShow( beans, response, key );
  	      return proveedores;
  	    }
  	  }

  	  public void xlsShow( Map<String, ProveedorDTO> beans, HttpServletResponse response,
  	      int key ) throws IOException
  	  {
  	    ServletContext context = getServletContext();
  	    InputStream istream = context
  	        .getResourceAsStream( "/WEB-INF/templates/PlantillaProveedor.xls");
  	    XLSTransformer transformer = new XLSTransformer();
  	    
  	    HSSFWorkbook workbook = transformer.transformXLS( istream, beans );

  	    response.setContentType( "application/vnd.ms-excel" );
  	    response.addHeader( "Content-Disposition",
  	        "attachment;filename=ReporteProveedor" + key + ".xls" );
  	    try (OutputStream os = response.getOutputStream())
  	    {
  	      workbook.write( os );
  	      os.flush();
  	    }
  	  }
}
