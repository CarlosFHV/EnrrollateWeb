package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class UserFormServlet
 */
@WebServlet(name = "UserForm", urlPatterns = { "/UserForm" })
public class UserFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int key = Integer.parseInt(request.getParameter("llave"));
        try {
            List<UserDTO> usuarios = getUsuario(key);

            request.setAttribute("usuarios", usuarios); // Cambiar el atributo a "usuarios"
        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/usuarios_view.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Obtener los usuarios registrados en la base de datos.
     */
    private List<UserDTO> getUsuario(int key) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        Connection connection = source.getConnection();

        try {
            return getUsuario(connection, key);
        } finally {
            connection.close();
        }
    }

    /**
     * Realizar la consulta sobre la base de datos, y regresar la lista de usuarios.
     */
    private List<UserDTO> getUsuario(Connection connection, int key) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT id_usuario, nombre, email, dirección, teléfono, rol, fecha_registro " +
                       "FROM usuarios " +
                       "WHERE id_usuario = " + key;
        ResultSet cursor = statement.executeQuery(query);

        List<UserDTO> usuarios = new ArrayList<UserDTO>();
        try {
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
            return usuarios;
        } finally {
            cursor.close();
        }
    }
}
