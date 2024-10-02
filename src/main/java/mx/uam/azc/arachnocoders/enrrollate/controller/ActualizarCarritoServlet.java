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

@WebServlet(name = "ActualizarCarritoServlet", urlPatterns = {"/ActualizarCarritoServlet"})
public class ActualizarCarritoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el ID del usuario desde la sesión
        int idUsuario = (int) request.getSession().getAttribute("idUsuario");

        try {
            // Establecer conexión con la base de datos a través del DataSource
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false); // Iniciar transacción

                // Procesar cada producto enviado en la solicitud
                for (String paramName : request.getParameterMap().keySet()) {
                    if (paramName.startsWith("cantidad_")) {
                        // Obtener el ID del producto
                        String idProductoStr = paramName.substring(9); // "cantidad_" tiene 9 caracteres
                        int idProducto = Integer.parseInt(idProductoStr);

                        // Obtener la nueva cantidad
                        int nuevaCantidad = Integer.parseInt(request.getParameter(paramName));

                        // Si la nueva cantidad es 0, eliminar el producto del carrito
                        if (nuevaCantidad == 0) {
                            eliminarProductoDelCarrito(connection, idUsuario, idProducto);
                        } else {
                            actualizarCantidadProducto(connection, idUsuario, idProducto, nuevaCantidad);
                        }
                    }
                }

                connection.commit(); // Confirmar transacción
            }

        } catch (NamingException | SQLException e) {
            throw new ServletException("Error al actualizar el carrito en la base de datos", e);
        }

        // Redireccionar de vuelta a la vista del carrito
        response.sendRedirect("verCarrito");
    }

    private void actualizarCantidadProducto(Connection connection, int idUsuario, int idProducto, int nuevaCantidad) throws SQLException {
        String query = "UPDATE carrito_producto SET Cantidad = ? WHERE ID_Carrito = (SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?) AND ID_Producto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, nuevaCantidad);
            stmt.setInt(2, idUsuario);
            stmt.setInt(3, idProducto);
            stmt.executeUpdate();
        }
    }

    private void eliminarProductoDelCarrito(Connection connection, int idUsuario, int idProducto) throws SQLException {
        String query = "DELETE FROM carrito_producto WHERE ID_Carrito = (SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?) AND ID_Producto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idProducto);
            stmt.executeUpdate();
        }
    }
}
