package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mx.uam.azc.arachnocoders.enrrollate.data.ProductoAdminDTO;

@WebServlet(name = "ExportarProductosPdfServlet", urlPatterns = { "/ExportarProductosPdfServlet" })
public class ExportarProductosPdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            java.util.List<ProductoAdminDTO> productos = getProductos();
            documentShow(productos, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private java.util.List<ProductoAdminDTO> getProductos() throws NamingException, SQLException {
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

    public void documentShow(java.util.List<ProductoAdminDTO> productos, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=ReporteProductos.pdf");
            OutputStream fos = response.getOutputStream();
            Document document = new Document(PageSize.LETTER.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.addTitle("Detalles de Productos");
            document.addAuthor("ArachnoCoders");
            document.addCreationDate();
            document.addSubject("Productos en PDF");
            document.addCreator("iText");
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, new Color(0, 0, 128));
            Phrase phrase = new Phrase("\n\nProductos\n", font);
            document.add(phrase);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell("ID Producto");
            table.addCell("Nombre Producto");
            table.addCell("Descripción");
            table.addCell("Precio");
            table.addCell("Stock");

            for (ProductoAdminDTO producto : productos) {
                table.addCell(String.valueOf(producto.getIdProducto()));
                table.addCell(producto.getNombreProducto());
                table.addCell(producto.getDescripcion());
                table.addCell(producto.getPrecio().toString());
                table.addCell(String.valueOf(producto.getStock()));
            }

            document.add(table);
            document.close();
            fos.flush();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
