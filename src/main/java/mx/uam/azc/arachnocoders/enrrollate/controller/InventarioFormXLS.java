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
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class InventarioFormXLS
 */
@WebServlet(name = "InventarioFormXLS", urlPatterns = { "/InventarioFormXLS" })

public class InventarioFormXLS extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InventarioFormXLS() {
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
    	      List<InventarioDTO> inventarios = getInventarios( key, response );
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
    	  private List<InventarioDTO> getInventarios( int key, HttpServletResponse response )
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
    	  private List<InventarioDTO> getInventarios( Connection connection, int key,
    	      HttpServletResponse response ) throws SQLException, IOException
    	  {
    	    String query = "SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario WHERE ID_Inventario = ?";
    	    try (PreparedStatement statement = connection.prepareStatement( query ))
    	    {
    	      statement.setInt( 1, key );
    	      ResultSet cursor = statement.executeQuery();

    	      List<InventarioDTO> inventarios = new ArrayList<>();
    	      Map<String, InventarioDTO> beans = new HashMap<>();
    	      while ( cursor.next() )
    	      {
    	    	  InventarioDTO inventario = new InventarioDTO();  
                  inventario.setIdInventario(cursor.getString(1));
                  inventario.setIdProducto(cursor.getString(2));
                  inventario.setCantidad(cursor.getString(3));
                  inventario.setUbicacion(cursor.getString(4));
                  inventarios.add(inventario);
    	        beans.put( "inventario", inventario );
    	      }
    	      xlsShow( beans, response, key );
    	      return inventarios;
    	      
    	    }
    	  }

    	  public void xlsShow( Map<String, InventarioDTO> beans, HttpServletResponse response,
    	      int key ) throws IOException
    	  {
    	    ServletContext context = getServletContext();
    	    InputStream istream = context
    	        .getResourceAsStream( "/WEB-INF/templates/PlantillaInventario.xls" );
    	    XLSTransformer transformer = new XLSTransformer();
    	    
    	    HSSFWorkbook workbook = transformer.transformXLS( istream, beans );

    	    response.setContentType( "application/vnd.ms-excel" );
    	    response.addHeader( "Content-Disposition",
    	        "attachment;filename=ReporteInventario" + key + ".xls" );
    	    try (OutputStream os = response.getOutputStream())
    	    {
    	      workbook.write( os );
    	      os.flush();
    	    }
    	  }
}
