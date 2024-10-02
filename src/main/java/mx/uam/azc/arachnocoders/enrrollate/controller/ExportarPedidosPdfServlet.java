package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
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

import mx.uam.azc.arachnocoders.enrrollate.data.Pedido;

@WebServlet(name = "ExportarPedidosPdfServlet", urlPatterns = { "/ExportarPedidosPdfServlet" })
public class ExportarPedidosPdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            java.util.List<Pedido> pedidos = getPedidos();
            documentShow(pedidos, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private java.util.List<Pedido> getPedidos() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        
        try (Connection connection = source.getConnection()) {
            return getPedidos(connection);
        }
    }

    private java.util.List<Pedido> getPedidos(Connection connection) throws SQLException {

        String query = "SELECT ID_Pedido, ID_Usuario, Fecha_Pedido, Estado, Dirección_Envio FROM Pedidos";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            java.util.List<Pedido> pedidos = new ArrayList<>();

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

    public void documentShow(java.util.List<Pedido> pedidos, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=ReportePedidos.pdf");
            OutputStream fos = response.getOutputStream();
            Document document = new Document(PageSize.LETTER.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.addTitle("Detalles de Pedidos");
            document.addAuthor("ArachnoCoders");
            document.addCreationDate();
            document.addSubject("Pedidos en PDF");
            document.addCreator("iText");
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, new Color(0, 0, 128));
            Phrase phrase = new Phrase("\n\nPedidos\n", font);
            document.add(phrase);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell("ID Pedido");
            table.addCell("ID Usuario");
            table.addCell("Fecha Pedido");
            table.addCell("Estado");
            table.addCell("Dirección de Envío");

            for (Pedido pedido : pedidos) {
                table.addCell(String.valueOf(pedido.getIdPedido()));
                table.addCell(String.valueOf(pedido.getIdUsuario()));
                table.addCell(pedido.getFechaPedido().toString());
                table.addCell(pedido.getEstado());
                table.addCell(pedido.getDireccionEnvio());
            }

            document.add(table);
            document.close();
            fos.flush();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
