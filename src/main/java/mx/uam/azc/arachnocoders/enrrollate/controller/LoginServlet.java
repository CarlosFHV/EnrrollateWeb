package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  

@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        
        try {
            if (authenticate(email, password)) {
                System.out.println("a");
                HttpSession session = request.getSession(); 

                // Obtener el idUsuario y guardarlo en la sesión
                Integer idUsuario = getUserIdByEmail(email);
                session.setAttribute("email", email);
                session.setAttribute("idUsuario", idUsuario); // Guarda el idUsuario en la sesión

                response.sendRedirect("VerProductos");
            } else {
                System.out.println("b");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private boolean authenticate(String email, String password) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE Email = ? AND Password = ?")) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Retorna verdadero si hay al menos un registro
                }
            }
        }
        return false; // Retorna falso si no se encuentra el usuario
    }

    // Nueva función para obtener el idUsuario por email
    private Integer getUserIdByEmail(String email) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT ID_Usuario FROM usuarios WHERE Email = ?")) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("ID_Usuario"); // Retorna el idUsuario si se encuentra
                }
            }
        }
        return null; // Retorna null si no se encuentra el usuario
    }
}
