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

import mx.uam.azc.arachnocoders.enrrollate.data.EnvioDTO;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Servlet implementation class ExportarEnviosXlsServlet
 */
@WebServlet(name = "ExportarEnviosXls", urlPatterns = { "/ExportarEnviosXls" })
public class ExportarEnviosXlsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<EnvioDTO> envios = getEnvios(response);
            request.setAttribute("envios", envios);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<EnvioDTO> getEnvios(HttpServletResponse response) throws NamingException, SQLException, IOException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getEnvios(connection, response);
        }
    }

    private List<EnvioDTO> getEnvios(Connection connection, HttpServletResponse response) throws SQLException, IOException {
        String query = "SELECT ID_Envio, ID_Pedido, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte FROM Envios";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            List<EnvioDTO> envios = new ArrayList<>();
            Map<String, List<EnvioDTO>> beans = new HashMap<>();
            while (cursor.next()) {
                EnvioDTO envio = new EnvioDTO();
                envio.setIdEnvio(cursor.getInt("ID_Envio"));
                envio.setIdPedido(cursor.getInt("ID_Pedido"));
                envio.setFechaEnvio(cursor.getTimestamp("Fecha_Envio"));
                envio.setFechaEntrega(cursor.getTimestamp("Fecha_Entrega"));
                envio.setEstadoEnvio(cursor.getString("Estado_Envio"));
                envio.setEmpresaTransporte(cursor.getString("Empresa_Transporte"));
                envios.add(envio);
            }
            beans.put("envios", envios);
            xlsShow(beans, response);
            return envios;
        }
    }

    public void xlsShow(Map<String, List<EnvioDTO>> beans, HttpServletResponse response) throws IOException {
        ServletContext context = getServletContext();
        InputStream istream = context.getResourceAsStream("/WEB-INF/templates/PlantillaEnvio.xls");
        XLSTransformer transformer = new XLSTransformer();
        HSSFWorkbook workbook = transformer.transformXLS(istream, beans);

        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteEnvios.xls");
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            os.flush();
        }
    }
}
