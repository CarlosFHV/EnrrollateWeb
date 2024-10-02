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

import mx.uam.azc.arachnocoders.enrrollate.data.EnvioDTO;

@WebServlet(name = "ExportarEnviosPdfServlet", urlPatterns = { "/ExportarEnviosPdfServlet" })
public class ExportarEnviosPdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            java.util.List<EnvioDTO> envios = getEnvios();
            documentShow(envios, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private java.util.List<EnvioDTO> getEnvios() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getEnvios(connection);
        }
    }

    private java.util.List<EnvioDTO> getEnvios(Connection connection) throws SQLException {
        String query = "SELECT ID_Envio, ID_Pedido, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte FROM Envios";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();

            java.util.List<EnvioDTO> envios = new ArrayList<>();

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

    public void documentShow(java.util.List<EnvioDTO> envios, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=ReporteEnvios.pdf");
            OutputStream fos = response.getOutputStream();
            Document document = new Document(PageSize.LETTER.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.addTitle("Detalles de Envíos");
            document.addAuthor("ArachnoCoders");
            document.addCreationDate();
            document.addSubject("Envíos en PDF");
            document.addCreator("iText");
            document.open();

            Font font = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, new Color(0, 0, 128));
            Phrase phrase = new Phrase("\n\nEnvíos\n", font);
            document.add(phrase);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            table.addCell("ID Envío");
            table.addCell("ID Pedido");
            table.addCell("Fecha Envío");
            table.addCell("Fecha Entrega");
            table.addCell("Estado Envío");
            table.addCell("Empresa Transporte");

            for (EnvioDTO envio : envios) {
                table.addCell(String.valueOf(envio.getIdEnvio()));
                table.addCell(String.valueOf(envio.getIdPedido()));
                table.addCell(envio.getFechaEnvio() != null ? envio.getFechaEnvio().toString() : "N/A");
                table.addCell(envio.getFechaEntrega() != null ? envio.getFechaEntrega().toString() : "N/A");
                table.addCell(envio.getEstadoEnvio());
                table.addCell(envio.getEmpresaTransporte());
            }

            document.add(table);
            document.close();
            fos.flush();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
