package mx.uam.azc.arachnocoders.enrrollate.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.naming.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.io.*;
import java.sql.*;
import java.util.*;

import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class UserFormServlet
 */
@WebServlet(name = "UserFormXls", urlPatterns = { "/UserFormXls" })
public class UserFormXlsServlet extends HttpServlet
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
      List<UserDTO> usuarios = getUsuarios( key, response );
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
  private List<UserDTO> getUsuarios( int key, HttpServletResponse response )
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
  private List<UserDTO> getUsuarios( Connection connection, int key,
      HttpServletResponse response ) throws SQLException, IOException
  {
    String query = "SELECT id_usuario, nombre, email, dirección, teléfono, rol, fecha_registro FROM usuarios WHERE id_usuario = ?";
    try (PreparedStatement statement = connection.prepareStatement( query ))
    {
      statement.setInt( 1, key );
      ResultSet cursor = statement.executeQuery();

      List<UserDTO> usuarios = new ArrayList<>();
      Map<String, UserDTO> beans = new HashMap<>();
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
        beans.put( "usuario", usuario );
      }
      xlsShow( beans, response, key );
      return usuarios;
    }
  }

  public void xlsShow( Map<String, UserDTO> beans, HttpServletResponse response,
      int key ) throws IOException
  {
    ServletContext context = getServletContext();
    InputStream istream = context
        .getResourceAsStream( "/WEB-INF/templates/PlantillaUsuario.xls" );
    XLSTransformer transformer = new XLSTransformer();
    HSSFWorkbook workbook = transformer.transformXLS( istream, beans );

    response.setContentType( "application/vnd.ms-excel" );
    response.addHeader( "Content-Disposition",
        "attachment;filename=ReporteUsuario" + key + ".xls" );
    try (OutputStream os = response.getOutputStream())
    {
      workbook.write( os );
      os.flush();
    }
  }
}
