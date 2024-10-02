package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

@WebServlet(name = "UserHTML", urlPatterns = { "/UserHTML" })
public class UserHTMLServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            java.util.List<UserDTO> usuarios = getUsuarios(response);
            request.setAttribute("usuarios", usuarios);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private java.util.List<UserDTO> getUsuarios(HttpServletResponse response) throws NamingException, SQLException, IOException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getUsuarios(connection, response);
        }
    }

    private java.util.List<UserDTO> getUsuarios(Connection connection, HttpServletResponse response) throws SQLException, IOException {
        String query = "SELECT id_usuario, nombre, email, dirección, teléfono, rol, fecha_registro FROM usuarios"; // Eliminar WHERE
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            java.util.List<UserDTO> usuarios = new ArrayList<>();

            while (cursor.next()) {
                UserDTO usuario = new UserDTO();
                usuario.setId(cursor.getInt("id_usuario"));
                usuario.setNombre(cursor.getString("nombre"));
                usuario.setEmail(cursor.getString("email"));
                usuario.setDireccion(cursor.getString("dirección"));
                usuario.setTelefono(cursor.getString("teléfono"));
                usuario.setRol(cursor.getString("rol"));
                usuario.setFechaRegistro(cursor.getString("fecha_registro"));
                usuarios.add(usuario);
            }
            documentShow(usuarios, response);
            return usuarios;
        }
    }

    public void documentShow(java.util.List<UserDTO> usuarios, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            response.addHeader("Content-Disposition", "attachment;filename=ReporteUsuarios.html");
            OutputStream fos = response.getOutputStream();

            // Construir el contenido HTML
            StringBuilder html = new StringBuilder();
            html.append("<html><head><title>Lista de Usuarios</title></head><body>");
            html.append("<h1>Lista de Usuarios</h1>");
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

            // Escribir el contenido HTML en la respuesta
            fos.write(html.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
