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
@WebServlet(name = "UserFormHtml", urlPatterns = { "/UserFormHtml" })
public class UserFormHtmlServlet extends HttpServlet
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

  public void documentShow(java.util.List<UserDTO> usuarios, HttpServletResponse response, int key) {
    try {
        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteUsuario" + key + ".html");
        OutputStream fos = response.getOutputStream();

        // Start building HTML content
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Detalles del Usuario</title></head><body>");
        html.append("<h1>Detalles del Usuario</h1>");
        
        // Create table headers
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
            .append("<th>ID</th>")
            .append("<th>Nombre</th>")
            .append("<th>Email</th>")
            .append("<th>Dirección</th>")
            .append("<th>Teléfono</th>")
            .append("<th>Rol</th>")
            .append("<th>Fecha Registro</th>")
            .append("</tr>");

        // Populate table rows
        for (UserDTO usuario : usuarios) {
            html.append("<tr>")
                .append("<td>").append(usuario.getId()).append("</td>")
                .append("<td>").append(usuario.getNombre()).append("</td>")
                .append("<td>").append(usuario.getEmail()).append("</td>")
                .append("<td>").append(usuario.getDireccion()).append("</td>")
                .append("<td>").append(usuario.getTelefono()).append("</td>")
                .append("<td>").append(usuario.getRol()).append("</td>")
                .append("<td>").append(usuario.getFechaRegistro()).append("</td>")
                .append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");

        // Write HTML content to response
        fos.write(html.toString().getBytes());
        fos.flush();
        fos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
