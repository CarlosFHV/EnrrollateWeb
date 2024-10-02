package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.io.Closeable;
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook; // Asegúrate de que estás usando HSSFWorkbook para .xls
import net.sf.jxls.transformer.XLSTransformer;
import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;


@WebServlet(name = "InventarioXLS", urlPatterns = { "/InventarioXLS" })
public class InventarioXLS extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<InventarioDTO> inventarios = getInventarios(response);
            if (inventarios.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay inventarios disponibles.");
                return;
            }
            xlsShow(inventarios, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar inventarios: " + e.getMessage(), e);
        }
    }

    private List<InventarioDTO> getInventarios(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getInventarios(connection);
        }
    }

    private List<InventarioDTO> getInventarios(Connection connection) throws SQLException {
        String query = "SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario ";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet cursor = statement.executeQuery()) {
            List<InventarioDTO> inventarios = new ArrayList<>();
            while (cursor.next()) {
                InventarioDTO inventario = new InventarioDTO();
                
                inventario.setIdInventario(cursor.getString(1));
                inventario.setIdProducto(cursor.getString(2));
                inventario.setCantidad(cursor.getString(3));
                inventario.setUbicacion(cursor.getString(4));
  
                inventarios.add(inventario);
            }
            return inventarios;
        }
    }

    public void xlsShow(List<InventarioDTO> inventarios, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaInventario.xls");
        if (istream == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Plantilla no encontrada.");
            return;
        }

        XLSTransformer transformer = new XLSTransformer();
        Map<String, Object> beans = new HashMap<>();
        beans.put("inventarios", inventarios);

        HSSFWorkbook workbook = null;
        try {
            workbook = transformer.transformXLS(istream, beans);
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=ReporteInventarios.xls");

            try (OutputStream os = response.getOutputStream()) {
                workbook.write(os);
                os.flush();
            }
        } catch (Exception e) {
            throw new IOException("Error al generar el archivo Excel: " + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                ((Closeable) workbook).close();
            }
        }
    }
}