package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.CarritoDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VerCarritoServlet", urlPatterns = {"/verCarrito"})
public class VerCarritoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el ID del usuario desde la sesión
        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario"); // Asegúrate de que el ID del usuario esté guardado en la sesión

        if (idUsuario == null) {
            // Maneja el caso donde el ID de usuario no esté disponible
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<CarritoDTO> carritoList = new ArrayList<>();

        try {
            // Establecer conexión con la base de datos a través del DataSource
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            try (Connection connection = dataSource.getConnection()) {
                // Consultar los productos en el carrito del usuario
                String query = "SELECT cp.ID_Producto, p.Nombre_Producto, cp.Cantidad FROM carrito_producto cp "
                             + "JOIN productos p ON cp.ID_Producto = p.ID_Producto "
                             + "WHERE cp.ID_Carrito IN (SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, idUsuario);
                    ResultSet resultSet = statement.executeQuery();
                    
                    while (resultSet.next()) {
                        int idProducto = resultSet.getInt("ID_Producto");
                        String nombre = resultSet.getString("Nombre_Producto");
                        int cantidad = resultSet.getInt("Cantidad");
                        carritoList.add(new CarritoDTO(idProducto, nombre, cantidad));
                    }
                }
            }

            // Pasar la lista de productos del carrito a la JSP
            request.setAttribute("carritoList", carritoList);
            request.getRequestDispatcher("/carrito_view.jsp").forward(request, response);

        } catch (NamingException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
