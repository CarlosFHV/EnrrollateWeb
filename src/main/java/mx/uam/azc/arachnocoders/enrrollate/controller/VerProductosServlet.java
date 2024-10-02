package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import mx.uam.azc.arachnocoders.enrrollate.data.ProductoDTO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VerProductosServlet", urlPatterns = { "/VerProductos" })
public class VerProductosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<ProductoDTO> productos = getProductos();
            request.setAttribute("productos", productos);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/productos_view.jsp");
        dispatcher.forward(request, response);
    }

    private List<ProductoDTO> getProductos() throws NamingException, SQLException {
        List<ProductoDTO> listaProductos = new ArrayList<>();
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet cursor = statement.executeQuery("SELECT ID_Producto, Nombre_Producto, Descripción, Precio, Stock, Url_Imagen FROM productos");
            
            while (cursor.next()) {
                ProductoDTO producto = new ProductoDTO();
                producto.setIdProducto(cursor.getString("ID_Producto"));
                producto.setNombre(cursor.getString("Nombre_Producto"));
                producto.setDescripcion(cursor.getString("Descripción"));
                producto.setPrecio(cursor.getDouble("Precio"));
                producto.setStock(cursor.getInt("Stock"));
                producto.setUrlImagen(cursor.getString("Url_Imagen"));
                System.out.println(cursor.getString("Url_Imagen"));
                listaProductos.add(producto);
            }
        } finally {
            connection.close();
        }
        return listaProductos;
    }
}
