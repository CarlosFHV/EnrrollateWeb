package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import mx.uam.azc.arachnocoders.enrrollate.data.ProductoAdminDTO;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class ExportarProductosXlsServlet
 */
@WebServlet(name = "ExportarProductosXlsServlet", urlPatterns = { "/ExportarProductosXlsServlet" })
public class ExportarProductosXlsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ProductoAdminDTO> productos = getProductos(response);
            request.setAttribute("productos", productos);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<ProductoAdminDTO> getProductos(HttpServletResponse response) throws NamingException, SQLException, IOException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getProductos(connection, response);
        }
    }

    private List<ProductoAdminDTO> getProductos(Connection connection, HttpServletResponse response) throws SQLException, IOException {
        String query = "SELECT ID_Producto, Nombre_Producto, Descripción, Precio, Fecha_Ingreso FROM Productos";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            List<ProductoAdminDTO> productos = new ArrayList<>();
            Map<String, List<ProductoAdminDTO>> beans = new HashMap<>();
            while (cursor.next()) {
                ProductoAdminDTO producto = new ProductoAdminDTO();
                producto.setIdProducto(cursor.getInt("ID_Producto"));
                producto.setNombreProducto(cursor.getString("Nombre_Producto"));
                producto.setDescripcion(cursor.getString("Descripción"));
                producto.setPrecio(cursor.getBigDecimal("Precio"));
                producto.setFechaIngreso(cursor.getTimestamp("Fecha_Ingreso"));
                productos.add(producto);
            }
            beans.put("productos", productos);
            xlsShow(beans, response);
            return productos;
        }
    }

    public void xlsShow(Map<String, List<ProductoAdminDTO>> beans, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaProducto.xls");
        XLSTransformer transformer = new XLSTransformer();
        HSSFWorkbook workbook = transformer.transformXLS(istream, beans);

        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteProductos.xls");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            os.flush();
        }
    }
}
