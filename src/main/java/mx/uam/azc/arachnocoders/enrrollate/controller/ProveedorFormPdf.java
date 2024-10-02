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
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;

/**
 * Servlet implementation class ProveedorFormPdf
 */
@WebServlet("/ProveedorFormPdf")
public class ProveedorFormPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public ProveedorFormPdf() {
      
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
  	    String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores WHERE ID_Proveedor= ?";
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

  	  public void documentShow( java.util.List<ProveedorDTO> proveedores,
  	      HttpServletResponse response, int key )
  	  {
  	    try
  	    {
  	      response.setContentType( "application/pdf" );
  	      response.addHeader( "Content-Disposition",
  	          "attachment;filename=ReporteProveedor" + key + ".pdf" );
  	      OutputStream fos = response.getOutputStream();
  	      Document document = new Document( PageSize.LETTER.rotate() );
  	      PdfWriter writer = PdfWriter.getInstance( document, fos );
  	      document.addTitle( "Detalles del Proveedor" );
  	      document.addAuthor( "ArachnoCoders" );
  	      document.addCreationDate();
  	      document.addSubject( "Proveedores en PDF" );
  	      document.addCreator( "iText" );
  	      document.open();

  	      Font font = FontFactory.getFont( FontFactory.COURIER, 18, Font.BOLD,
  	          new Color( 0, 0, 128 ) );
  	      Phrase phrase = new Phrase( "\n\nDetalles del Proveedor\n", font );
  	      Table tabla = new Table( 5 );

  	      tabla.setBorderWidth( 3 );
  	      tabla.setBorderColor( new Color( 0, 0, 255 ) );
  	      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
  	      tabla.setPadding( 5 );
  	      tabla.setSpacing( 5 );

  	      Cell celda = new Cell( "Proveedor" );
  	      celda.setHeader( true );
  	      celda.setColspan( 5 );
  	      celda.setBorderColor( new Color( 0, 192, 0 ) );
  	      tabla.addCell( celda );
  	      document.add( tabla );

  	      tabla = new Table( 5 );
  	      tabla.setBorderWidth( 3 );
  	      tabla.setBorderColor( new Color( 0, 0, 255 ) );
  	      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
  	      tabla.setPadding( 5 );
  	      tabla.setSpacing( 5 );

  	      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
  	          new Color( 64, 64, 255 ) );
  	      phrase = new Phrase( "ID Proveedor", font );
  	      tabla.addCell( phrase );
  	      phrase = new Phrase( "Nombre Proveedor", font );
  	      tabla.addCell( phrase );
  	      phrase = new Phrase( "Contacto", font );
  	      tabla.addCell( phrase );
  	      phrase = new Phrase( "Telefono", font );
  	      tabla.addCell( phrase );
  	      phrase = new Phrase( "Email", font );
	      tabla.addCell( phrase );
	   
  	      document.add( tabla );

  	      tabla = new Table( 5 );
  	      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
  	          new Color( 0, 128, 0 ) );

  	      for ( ProveedorDTO proveedor : proveedores )
  	      {
  	        phrase = new Phrase( String.valueOf( proveedor.getIdProveedor() ), font );
  	        tabla.addCell( phrase );
  	        phrase = new Phrase( proveedor.getNombreProveedor() , font );
  	        tabla.addCell( phrase );
  	        phrase = new Phrase( proveedor.getContacto(), font );
  	        tabla.addCell( phrase );
  	        phrase = new Phrase( proveedor.getTelefono(), font );
	        tabla.addCell( phrase );
  	        phrase = new Phrase( proveedor.getEmail(), font );
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
