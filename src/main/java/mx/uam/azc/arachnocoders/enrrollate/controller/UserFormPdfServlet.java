package mx.uam.azc.arachnocoders.enrrollate.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

/**
 * Servlet implementation class UserFormServlet
 */
@WebServlet(name = "UserFormPdf", urlPatterns = { "/UserFormPdf" })
public class UserFormPdfServlet extends HttpServlet
{
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doGet( HttpServletRequest request,
      HttpServletResponse response ) throws ServletException, IOException
  {
    // Obtener el parámetro "id_usuario"
    String idUsuarioStr = request.getParameter( "id_usuario" );

    // Verificar si el parámetro es nulo o está vacío
    if ( idUsuarioStr == null || idUsuarioStr.trim().isEmpty() )
    {
      // Manejar el caso donde el parámetro no está presente o está vacío
      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
          "El parámetro 'id_usuario' es requerido." );
      return;
    }

    int key;
    try
    {
      key = Integer.parseInt( idUsuarioStr );
    }
    catch ( NumberFormatException e )
    {
      // Manejar el caso donde el parámetro no puede ser convertido a un entero
      response.sendError( HttpServletResponse.SC_BAD_REQUEST,
          "El parámetro 'id_usuario' no es un número válido." );
      return;
    }

    try
    {
      java.util.List<UserDTO> usuarios = getUsuarios( key, response );
      request.setAttribute( "usuarios", usuarios );
    }
    catch ( Exception e )
    {
      throw new ServletException( e );
    }
  }

  /**
   * Obtener los usuarios registrados en la base de datos.
   */
  private java.util.List<UserDTO> getUsuarios( int key,
      HttpServletResponse response )
      throws NamingException, SQLException, IOException
  {
    Context context = new InitialContext();
    DataSource source = ( DataSource )context
        .lookup( "java:comp/env/jdbc/TestDS" );

    try (Connection connection = source.getConnection())
    {
      return getUsuarios( connection, key, response );
    }
  }

  /**
   * Realizar la consulta sobre la base de datos, y regresar la lista de
   * usuarios.
   */
  private java.util.List<UserDTO> getUsuarios( Connection connection, int key,
      HttpServletResponse response ) throws SQLException, IOException
  {
    String query = "SELECT id_usuario, nombre, email, dirección, teléfono, rol, fecha_registro FROM usuarios WHERE id_usuario = ?";
    try (PreparedStatement statement = connection.prepareStatement( query ))
    {
      statement.setInt( 1, key );
      ResultSet cursor = statement.executeQuery();

      java.util.List<UserDTO> usuarios = new ArrayList<>();

      while ( cursor.next() )
      {
        UserDTO usuario = new UserDTO();
        usuario.setId( cursor.getInt( "id_usuario" ) );
        usuario.setNombre( cursor.getString( "nombre" ) );
        usuario.setEmail( cursor.getString( "email" ) );
        usuario.setDireccion( cursor.getString( "dirección" ) );
        usuario.setTelefono( cursor.getString( "teléfono" ) );
        usuario.setRol( cursor.getString( "rol" ) );
        usuario.setFechaRegistro( cursor.getString( "fecha_registro" ) );
        usuarios.add( usuario );
      }
      documentShow( usuarios, response, key );
      return usuarios;
    }
  }

  public void documentShow( java.util.List<UserDTO> usuarios,
      HttpServletResponse response, int key )
  {
    try
    {
      response.setContentType( "application/pdf" );
      response.addHeader( "Content-Disposition",
          "attachment;filename=ReporteUsuario" + key + ".pdf" );
      OutputStream fos = response.getOutputStream();
      Document document = new Document( PageSize.LETTER.rotate() );
      PdfWriter writer = PdfWriter.getInstance( document, fos );
      document.addTitle( "Detalles del usuario" );
      document.addAuthor( "ArachnoCoders" );
      document.addCreationDate();
      document.addSubject( "Usuarios en PDF" );
      document.addCreator( "iText" );
      document.open();

      Font font = FontFactory.getFont( FontFactory.COURIER, 18, Font.BOLD,
          new Color( 0, 0, 128 ) );
      Phrase phrase = new Phrase( "\n\nDetalles del Usuario\n", font );
      Table tabla = new Table( 7 );

      tabla.setBorderWidth( 3 );
      tabla.setBorderColor( new Color( 0, 0, 255 ) );
      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
      tabla.setPadding( 5 );
      tabla.setSpacing( 5 );

      Cell celda = new Cell( "Usuarios" );
      celda.setHeader( true );
      celda.setColspan( 7 );
      celda.setBorderColor( new Color( 0, 192, 0 ) );
      tabla.addCell( celda );
      document.add( tabla );

      tabla = new Table( 7 );
      tabla.setBorderWidth( 3 );
      tabla.setBorderColor( new Color( 0, 0, 255 ) );
      tabla.setBackgroundColor( new Color( 226, 222, 222 ) );
      tabla.setPadding( 5 );
      tabla.setSpacing( 5 );

      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
          new Color( 64, 64, 255 ) );
      phrase = new Phrase( "ID", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Nombre", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Email", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Dirección", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Teléfono", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Rol", font );
      tabla.addCell( phrase );
      phrase = new Phrase( "Fecha Registro", font );
      tabla.addCell( phrase );
      document.add( tabla );

      tabla = new Table( 7 );
      font = FontFactory.getFont( FontFactory.COURIER, 8, Font.BOLD,
          new Color( 0, 128, 0 ) );

      for ( UserDTO usuario : usuarios )
      {
        phrase = new Phrase( String.valueOf( usuario.getId() ), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getNombre(), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getEmail(), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getDireccion(), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getTelefono(), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getRol(), font );
        tabla.addCell( phrase );
        phrase = new Phrase( usuario.getFechaRegistro(), font );
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
