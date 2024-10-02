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

import mx.uam.azc.arachnocoders.enrrollate.data.EnvioDTO; // Asegúrate de tener la clase EnvioDTO

@WebServlet(name = "ExportarEnviosHtmlServlet", urlPatterns = { "/ExportarEnviosHtmlServlet" })
public class ExportarEnviosHtmlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<EnvioDTO> envios = getEnvios(response);
            if (envios.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay envíos disponibles.");
                return;
            }
            documentShow(envios, response);
        } catch (Exception e) {
            throw new ServletException("Error al exportar envíos: " + e.getMessage(), e);
        }
    }

    private List<EnvioDTO> getEnvios(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getEnvios(connection);
        }
    }

    private List<EnvioDTO> getEnvios(Connection connection) throws SQLException {
        String query = "SELECT ID_Envio, ID_Pedido, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte FROM Envios";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet cursor = statement.executeQuery()) {
            List<EnvioDTO> envios = new ArrayList<>();
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
            return envios;
        }
    }

    public void documentShow(List<EnvioDTO> envios, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment;filename=ReporteEnvios.html");
        OutputStream fos = response.getOutputStream();

        // Start building HTML content
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Reporte de Envíos</title></head><body>");
        html.append("<h1>Reporte de Envíos</h1>");

        // Create table headers
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
            .append("<th>ID Envío</th>")
            .append("<th>ID Pedido</th>")
            .append("<th>Fecha Envío</th>")
            .append("<th>Fecha Entrega</th>")
            .append("<th>Estado Envío</th>")
            .append("<th>Empresa de Transporte</th>")
            .append("</tr>");

        // Populate table rows
        for (EnvioDTO envio : envios) {
            html.append("<tr>")
                .append("<td>").append(envio.getIdEnvio()).append("</td>")
                .append("<td>").append(envio.getIdPedido()).append("</td>")
                .append("<td>").append(envio.getFechaEnvio()).append("</td>")
                .append("<td>").append(envio.getFechaEntrega()).append("</td>")
                .append("<td>").append(envio.getEstadoEnvio()).append("</td>")
                .append("<td>").append(envio.getEmpresaTransporte()).append("</td>")
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
