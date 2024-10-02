package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.io.OutputStream;
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

import mx.uam.azc.arachnocoders.enrrollate.data.ProductoAdminDTO; // Asegúrate de tener la clase ProductoDTO

@WebServlet(name = "ExportarProductosHtmlServlet", urlPatterns = { "/ExportarProductosHtmlServlet" })
public class ExportarProductosHtmlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProductoAdminDTO> productos = getProductos(response);
            if (productos.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay productos disponibles.");
                return;
            }
            documentShow(productos, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar productos: " + e.getMessage(), e);
        }
    }

    private List<ProductoAdminDTO> getProductos(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getProductos(connection);
        }
    }

    private java.util.List<ProductoAdminDTO> getProductos(Connection connection) throws SQLException {
        String query = "SELECT id_Producto, nombre_Producto, descripción, precio, stock, fecha_Ingreso FROM Productos";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            java.util.List<ProductoAdminDTO> productos = new ArrayList<>();

            while (cursor.next()) {
            	ProductoAdminDTO producto = new ProductoAdminDTO();
                producto.setIdProducto(cursor.getInt("id_Producto"));
                producto.setNombreProducto(cursor.getString("nombre_Producto"));
                producto.setDescripcion(cursor.getString("descripción"));
                producto.setPrecio(cursor.getBigDecimal("precio"));
                producto.setStock(cursor.getInt("stock"));
                producto.setFechaIngreso(cursor.getTimestamp("fecha_Ingreso"));
                productos.add(producto);
            }
            return productos;
        }
    }

    public void documentShow(List<ProductoAdminDTO> productos, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteProductos.html");
        OutputStream fos = response.getOutputStream();

        // Start building HTML content
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Reporte de Productos</title></head><body>");
        html.append("<h1>Reporte de Productos</h1>");

        // Create table headers
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
            .append("<th>ID Producto</th>")
            .append("<th>Nombre Producto</th>")
            .append("<th>Descripción</th>")
            .append("<th>Precio</th>")
            .append("<th>Stock</th>")
            .append("<th>Fecha de Ingreso</th>")
            .append("</tr>");

        // Populate table rows
        for (ProductoAdminDTO producto : productos) {
            html.append("<tr>")
                .append("<td>").append(producto.getIdProducto()).append("</td>")
                .append("<td>").append(producto.getNombreProducto()).append("</td>")
                .append("<td>").append(producto.getDescripcion()).append("</td>")
                .append("<td>").append(producto.getPrecio()).append("</td>")
                .append("<td>").append(producto.getStock()).append("</td>")
                .append("<td>").append(producto.getFechaIngreso()).append("</td>")
                .append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");

        // Write HTML content to response
        fos.write(html.toString().getBytes());
        fos.flush();
        fos.close();
    }
}
