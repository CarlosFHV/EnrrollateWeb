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
 * Servlet implementation class ProveedorPDF
 */
@WebServlet("/ProveedorPDF")
public class ProveedorPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProveedorPDF() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet( HttpServletRequest request,
    	      HttpServletResponse response ) throws ServletException, IOException
    	  {
    	    
    	    try
    	    {
    	      getProveedor(  response );
    	     
    	    }
    	    catch ( Exception e )
    	    {
    	      throw new ServletException( e );
    	    }
    	  }

    	  /**
    	   * Obtener los usuarios registrados en la base de datos.
    	   */
    	  private void getProveedor(
    	      HttpServletResponse response )
    	      throws NamingException, SQLException, IOException
    	  {
    	    Context context = new InitialContext();
    	    DataSource source = ( DataSource )context
    	        .lookup( "java:comp/env/jdbc/TestDS" );

    	    try (Connection connection = source.getConnection())
    	    {
    	       getProveedor( connection, response );
    	    }
    	  }

    	  /**
    	   * Realizar la consulta sobre la base de datos, y regresar la lista de
    	   * usuarios.
    	   */
    	  private void  getProveedor( Connection connection,
    	      HttpServletResponse response ) throws SQLException, IOException
    	  {
    	    String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores";
    	    try (PreparedStatement statement = connection.prepareStatement( query ))
    	    {
    	   
    	      ResultSet cursor = statement.executeQuery();

    	      java.util.List<ProveedorDTO> proveedores = new ArrayList<>();

    	    try {
              
    	    	while (cursor.next()) {
                    ProveedorDTO proveedor = new ProveedorDTO();
                    proveedor.setIdProveedor(cursor.getString(1));
                    proveedor.setNombreProveedor(cursor.getString(2));
                    proveedor.setContacto(cursor.getString(3));
                    proveedor.setTelefono(cursor.getString(4));
                    proveedor.setEmail(cursor.getString(5));
                    
                    proveedores.add(proveedor);
                 
                }
              
          } finally {
          cursor.close();
          }
    	      documentShow( proveedores, response);
    	     
    	    }
    	  }

    	  public void documentShow( java.util.List<ProveedorDTO> proveedores,
    	      HttpServletResponse response)
    	  {
    	    try
    	    {
    	      response.setContentType( "application/pdf" );
    	      response.addHeader( "Content-Disposition",
    	          "attachment;filename=ReporteProveedor" +  ".pdf" );
    	      OutputStream fos = response.getOutputStream();
    	      Document document = new Document( PageSize.LETTER.rotate() );
    	      PdfWriter writer = PdfWriter.getInstance( document, fos );
    	      document.addTitle( "Detalles del Proveedor" );
    	      document.addAuthor( "ArachnoCoders" );
    	      document.addCreationDate();
    	      document.addSubject( "Proveedor en PDF" );
    	      document.addCreator( "iText" );
    	      document.open();

    	      Font font = FontFactory.getFont( FontFactory.COURIER, 18, Font.BOLD,
    	          new Color( 0, 0, 128 ) );
    	      Phrase phrase = new Phrase( "\n\nDetalles del Proveedor\n", font );
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

    	      tabla = new Table( 4 );
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
