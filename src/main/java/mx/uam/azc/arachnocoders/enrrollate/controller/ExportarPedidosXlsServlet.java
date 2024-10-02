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
import mx.uam.azc.arachnocoders.enrrollate.data.Pedido;

@WebServlet(name = "ExportarPedidosXlsServlet", urlPatterns = { "/ExportarPedidosXlsServlet" })
public class ExportarPedidosXlsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Pedido> pedidos = getPedidos(response);
            if (pedidos.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay pedidos disponibles.");
                return;
            }
            xlsShow(pedidos, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar pedidos: " + e.getMessage(), e);
        }
    }

    private List<Pedido> getPedidos(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getPedidos(connection);
        }
    }

    private List<Pedido> getPedidos(Connection connection) throws SQLException {
        String query = "SELECT ID_Pedido, ID_Usuario, Fecha_Pedido, Estado, Dirección_Envio FROM Pedidos";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet cursor = statement.executeQuery()) {
            List<Pedido> pedidos = new ArrayList<>();
            while (cursor.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(cursor.getInt("ID_Pedido"));
                pedido.setIdUsuario(cursor.getInt("ID_Usuario"));
                pedido.setFechaPedido(cursor.getTimestamp("Fecha_Pedido"));
                pedido.setEstado(cursor.getString("Estado"));
                pedido.setDireccionEnvio(cursor.getString("Dirección_Envio"));
                pedidos.add(pedido);
            }
            return pedidos;
        }
    }

    public void xlsShow(List<Pedido> pedidos, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaPedido.xls");
        if (istream == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Plantilla no encontrada.");
            return;
        }

        XLSTransformer transformer = new XLSTransformer();
        Map<String, Object> beans = new HashMap<>();
        beans.put("pedidos", pedidos);

        HSSFWorkbook workbook = null;
        try {
            workbook = transformer.transformXLS(istream, beans);
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=ReportePedidos.xls");

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
