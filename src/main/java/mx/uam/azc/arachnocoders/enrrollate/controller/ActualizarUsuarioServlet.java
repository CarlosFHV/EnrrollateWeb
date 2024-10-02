package mx.uam.azc.arachnocoders.enrrollate.controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ActualizarUsuario")
public class ActualizarUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("ID_Usuario"));
        String nombre = request.getParameter("Nombre");
        String email = request.getParameter("Email");
        String password = request.getParameter("Password");
        String direccion = request.getParameter("Dirección");
        String telefono = request.getParameter("Teléfono");
        
        // Configuración de la conexión a la base de datos
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Obtén el contexto y la fuente de datos
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

            // Abre la conexión
            connection = dataSource.getConnection();

            // Consulta SQL para actualizar la información del usuario
            String query = "UPDATE usuarios SET Nombre = ?, Email = ?, Password = ?, Dirección = ?, Teléfono = ? WHERE ID_Usuario = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, direccion);
            statement.setString(5, telefono);
            statement.setInt(6, idUsuario);

            // Ejecuta la actualización
            int filasActualizadas = statement.executeUpdate();
            if (filasActualizadas > 0) {
                response.sendRedirect("actualizar_usuario.jsp?mensaje=Usuario actualizado exitosamente.");
            } else {
                response.sendRedirect("actualizar_usuario.jsp?mensaje=No se pudo actualizar el usuario.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("actualizar_usuario.jsp?mensaje=Error al acceder a la base de datos: " + e.getMessage());
        } catch (NamingException e) {
            e.printStackTrace();
            response.sendRedirect("actualizar_usuario.jsp?mensaje=Error en la configuración de la base de datos: " + e.getMessage());
        } finally {
            // Cierra los recursos
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
