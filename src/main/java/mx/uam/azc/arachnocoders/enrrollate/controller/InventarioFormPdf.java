package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.awt.Color;
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

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

/**
 * Servlet implementation class InventarioFormPdf
 */
@WebServlet("/InventarioFormPdf")
public class InventarioFormPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public InventarioFormPdf() {

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
    	      documentShow( inventarios, response, key );
    	      return inventarios;
    	    }
    	  }

    	  public void documentShow( java.util.List<InventarioDTO> inventarios,
    	      HttpServletResponse response, int key )
    	  {
    	    try
    	    {
    	      response.setContentType( "application/pdf" );
    	      response.addHeader( "Content-Disposition",
    	          "attachment;filename=ReporteInventario" + key + ".pdf" );
    	      OutputStream fos = response.getOutputStream();
    	      Document document = new Document( PageSize.LETTER.rotate() );
    	      PdfWriter writer = PdfWriter.getInstance( document, fos );
    	      document.addTitle( "Detalles del Inventario" );
    	      document.addAuthor( "ArachnoCoders" );
    	      document.addCreationDate();
    	      document.addSubject( "Inventarios en PDF" );
    	      document.addCreator( "iText" );
    	      document.open();

    	      Font font = FontFactory.getFont( FontFactory.COURIER, 18, Font.BOLD,
    	          new Color( 0, 0, 128 ) );
    	      Phrase phrase = new Phrase( "\n\nDetalles del Inventario\n", font );
    	      Table tabla = new Table( 4 );

    	      tabla.setBorderWidth( 3 );
    	      tabla.setBorderColor( new Color( 0, 0, 255 ) );
    	      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
    	      tabla.setPadding( 5 );
    	      tabla.setSpacing( 5 );

    	      Cell celda = new Cell( "Inventarios" );
    	      celda.setHeader( true );
    	      celda.setColspan( 4 );
    	      celda.setBorderColor( new Color( 0, 192, 0 ) );
    	      tabla.addCell( celda );
    	      document.add( tabla );

    	      tabla = new Table( 4 );
    	      tabla.setBorderWidth( 3 );
    	      tabla.setBorderColor( new Color( 0, 0, 255 ) );
    	      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
    	      tabla.setPadding( 5 );
    	      tabla.setSpacing( 5 );

    	      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
    	          new Color( 64, 64, 255 ) );
    	      phrase = new Phrase( "ID Inventario", font );
    	      tabla.addCell( phrase );
    	      phrase = new Phrase( "ID Producto", font );
    	      tabla.addCell( phrase );
    	      phrase = new Phrase( "Cantidad Disponible", font );
    	      tabla.addCell( phrase );
    	      phrase = new Phrase( "Ubicacion de Almacen", font );
    	      tabla.addCell( phrase );
    	   
    	      document.add( tabla );

    	      tabla = new Table( 4 );
    	      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
    	          new Color( 0, 128, 0 ) );

    	      for ( InventarioDTO inventario : inventarios )
    	      {
    	        phrase = new Phrase( String.valueOf( inventario.getIdInventario() ), font );
    	        tabla.addCell( phrase );
    	        phrase = new Phrase( inventario.getIdProducto() , font );
    	        tabla.addCell( phrase );
    	        phrase = new Phrase( inventario.getCantidad(), font );
    	        tabla.addCell( phrase );
    	        phrase = new Phrase( inventario.getUbicacion(), font );
    	        tabla.addCell( phrase );
    	        
    	      }
    	      document.add( tabla );
    	      fos.flush();
    	      document.close();
    	    }
    	    catch ( IOException e )
    	    {
    	      e.printStackTrace();
    	    }
    	    catch ( DocumentException e )
    	    {
    	      e.printStackTrace();
    	    }
    	  }

}
