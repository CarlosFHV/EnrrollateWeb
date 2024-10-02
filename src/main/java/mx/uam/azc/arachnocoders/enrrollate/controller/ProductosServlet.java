package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.ProductoAdminDTO;

@WebServlet(name = "ProductosServlet", urlPatterns = { "/ProductosServlet" })
public class ProductosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProductoAdminDTO> productos = getProductos();
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("productos_admin.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Error al obtener productos: " + e.getMessage(), e);
        }
    }

    private List<ProductoAdminDTO> getProductos() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            String query = "SELECT ID_Producto, Nombre_Producto, Descripción, Precio, Stock FROM Productos";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet cursor = statement.executeQuery()) {
                List<ProductoAdminDTO> productos = new ArrayList<>();
                while (cursor.next()) {
                	ProductoAdminDTO producto = new ProductoAdminDTO();
                    producto.setIdProducto(cursor.getInt("ID_Producto"));
                    producto.setNombreProducto(cursor.getString("Nombre_Producto"));
                    producto.setDescripcion(cursor.getString("Descripción"));
                    producto.setPrecio(cursor.getBigDecimal("Precio"));
                    producto.setStock(cursor.getInt("Stock"));          
                    productos.add(producto);
                }
                return productos;
            }
        }
    }
}
