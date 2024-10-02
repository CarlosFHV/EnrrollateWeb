package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "ActualizarProductoServlet", urlPatterns = { "/ActualizarProductoServlet" })
public class ActualizarProductoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        // Aquí se puede agregar lógica para mostrar el formulario de actualización si es necesario
        request.setAttribute("id", id);
        request.getRequestDispatcher("productos_admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id")); // Cambiado a "id"
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String precioStr = request.getParameter("precio");
            int stock = Integer.parseInt(request.getParameter("stock"));
            double precio = precioStr != null && !precioStr.trim().isEmpty() ? Double.parseDouble(precioStr) : 0.0;

            updateProducto(id, nombre, descripcion, precio, stock);
            response.sendRedirect("ProductosServlet"); // Redirigir a la lista de productos
        } catch (NumberFormatException e) {
            throw new ServletException("Error al actualizar producto: formato de número inválido", e);
        } catch (Exception e) {
            throw new ServletException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    private void updateProducto(int id, String nombre, String descripcion, double precio, int stock) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            String updateQuery = "UPDATE Productos SET Nombre_Producto = ?, Descripción = ?, Precio = ? , Stock = ? WHERE ID_Producto = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setDouble(3, precio);
                statement.setInt(4, stock);
                statement.setInt(5, id);
                statement.executeUpdate();
            }
        }
    }
}
