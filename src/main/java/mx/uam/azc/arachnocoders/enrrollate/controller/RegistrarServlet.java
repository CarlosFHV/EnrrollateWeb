package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class RegistrarServlet
 */
@WebServlet(name = "RegistrarServlet", urlPatterns = {"/RegistrarServlet"})
public class RegistrarServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            connection = dataSource.getConnection();

            String sql = "INSERT INTO `enrrollateweb`.`usuarios` " +
                         "(`Nombre`, `Email`, `Password`, `Dirección`, `Teléfono`, `Rol`, `Fecha_Registro`) " +
                         "VALUES (?, ?, ?, ?, ?, 'cliente', CURRENT_TIMESTAMP)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, direccion);
            statement.setString(5, telefono);
            
            int result = statement.executeUpdate();
            if (result > 0) {
                response.sendRedirect("login.jsp"); // Redirige a la página de login o confirmación
            } else {
                // Manejo de error: no se insertó
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
