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

import mx.uam.azc.arachnocoders.enrrollate.data.Pedido; 

@WebServlet(name = "ExportarPedidosHtmlServlet", urlPatterns = { "/ExportarPedidosHtmlServlet" })
public class ExportarPedidosHtmlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Pedido> pedidos = getPedidos(response);
            if (pedidos.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay pedidos disponibles.");
                return;
            }
            documentShow(pedidos, response);
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

    public void documentShow(List<Pedido> pedidos, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.addHeader("Content-Disposition", "attachment;filename=ReportePedidos.html");
        OutputStream fos = response.getOutputStream();

        // Start building HTML content
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Reporte de Pedidos</title></head><body>");
        html.append("<h1>Reporte de Pedidos</h1>");
        
        // Create table headers
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr>")
            .append("<th>ID Pedido</th>")
            .append("<th>ID Usuario</th>")
            .append("<th>Fecha Pedido</th>")
            .append("<th>Estado</th>")
            .append("<th>Dirección de Envío</th>")
            .append("</tr>");

        // Populate table rows
        for (Pedido pedido : pedidos) {
            html.append("<tr>")
                .append("<td>").append(pedido.getIdPedido()).append("</td>")
                .append("<td>").append(pedido.getIdUsuario()).append("</td>")
                .append("<td>").append(pedido.getFechaPedido()).append("</td>")
                .append("<td>").append(pedido.getEstado()).append("</td>")
                .append("<td>").append(pedido.getDireccionEnvio()).append("</td>")
                .append("</tr>");
        }
        html.append("</table>");
        html.append("</body></html>");


        fos.write(html.toString().getBytes());
        fos.flush();
        fos.close();
    }
}
