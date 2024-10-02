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

@WebServlet(name = "CarritoServlet", urlPatterns = {"/agregarCarrito"})
public class CarritoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Obtener el ID del producto y la cantidad del formulario
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));

        // Obtener el ID del usuario desde la sesión
        HttpSession session = request.getSession();
        Integer idUsuario = (Integer) session.getAttribute("idUsuario"); // Asegúrate de que el ID del usuario esté guardado en la sesión

        if (idUsuario == null) {
            // Maneja el caso donde el ID de usuario no esté disponible
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Establecer conexión con la base de datos a través del DataSource
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            try (Connection connection = dataSource.getConnection()) {
                // Verificar si el usuario ya tiene un carrito
                String queryCarrito = "SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?";
                Integer idCarrito = null;

                try (PreparedStatement stmtCarrito = connection.prepareStatement(queryCarrito)) {
                    stmtCarrito.setInt(1, idUsuario);
                    ResultSet rsCarrito = stmtCarrito.executeQuery();
                    if (rsCarrito.next()) {
                        idCarrito = rsCarrito.getInt("ID_Carrito");
                    } else {
                        // Si no hay carrito, crear uno nuevo
                        String queryInsertCarrito = "INSERT INTO carrito (ID_Usuario) VALUES (?)";
                        try (PreparedStatement stmtInsertCarrito = connection.prepareStatement(queryInsertCarrito, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            stmtInsertCarrito.setInt(1, idUsuario);
                            stmtInsertCarrito.executeUpdate();
                            // Obtener el ID del carrito recién creado
                            try (ResultSet generatedKeys = stmtInsertCarrito.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    idCarrito = generatedKeys.getInt(1);
                                }
                            }
                        }
                    }
                }

                // Ahora verifica si el producto ya está en el carrito
                String queryCheck = "SELECT Cantidad FROM carrito_producto WHERE ID_Carrito = ? AND ID_Producto = ?";
                try (PreparedStatement statement = connection.prepareStatement(queryCheck)) {
                    statement.setInt(1, idCarrito);
                    statement.setInt(2, idProducto);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        // Si el producto ya existe, actualizar la cantidad
                        int nuevaCantidad = resultSet.getInt("Cantidad") + cantidad;
                        String queryUpdate = "UPDATE carrito_producto SET Cantidad = ? WHERE ID_Carrito = ? AND ID_Producto = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(queryUpdate)) {
                            updateStatement.setInt(1, nuevaCantidad);
                            updateStatement.setInt(2, idCarrito);
                            updateStatement.setInt(3, idProducto);
                            updateStatement.executeUpdate();
                        }
                    } else {
                        // Si el producto no está en el carrito, insertarlo
                        String queryInsert = "INSERT INTO carrito_producto (ID_Carrito, ID_Producto, Cantidad) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(queryInsert)) {
                            insertStatement.setInt(1, idCarrito);
                            insertStatement.setInt(2, idProducto);
                            insertStatement.setInt(3, cantidad);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }

            // Redirigir a una página de éxito o al carrito
            response.sendRedirect(request.getContextPath() + "/VerProductos");

        } catch (NamingException | SQLException e) {
            throw new ServletException(e);
        }
    }
}
