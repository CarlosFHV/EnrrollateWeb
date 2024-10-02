package mx.uam.azc.arachnocoders.enrrollate.controller;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

@WebServlet("/UserPDF")
public class UserPDFServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<UserDTO> usuarios = getUsuarios(response);
            documentShow(usuarios, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<UserDTO> getUsuarios(HttpServletResponse response) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        try (Connection connection = source.getConnection()) {
            return getUsuarios(connection);
        }
    }

    private List<UserDTO> getUsuarios(Connection connection) throws SQLException {
        String query = "SELECT id_usuario, nombre, email, dirección, teléfono, rol, fecha_registro FROM usuarios";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet cursor = statement.executeQuery();
            List<UserDTO> usuarios = new ArrayList<>();

            while (cursor.next()) {
                UserDTO usuario = new UserDTO();
                usuario.setId(cursor.getInt("id_usuario"));
                usuario.setNombre(cursor.getString("nombre"));
                usuario.setEmail(cursor.getString("email"));
                usuario.setDireccion(cursor.getString("dirección"));
                usuario.setTelefono(cursor.getString("teléfono"));
                usuario.setRol(cursor.getString("rol"));
                usuario.setFechaRegistro(cursor.getString("fecha_registro"));
                usuarios.add(usuario);
            }
            return usuarios;
        }
    }

    public void documentShow(List<UserDTO> usuarios, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment;filename=ReporteUsuarios.pdf");
            OutputStream fos = response.getOutputStream();
            Document document = new Document(PageSize.LETTER.rotate());
            PdfWriter.getInstance(document, fos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD, new Color(0, 0, 128));
            Phrase title = new Phrase("Detalles de los Usuarios\n", titleFont);
            document.add(title);

            Table table = new Table(7);
            table.setPadding(5);
            table.setSpacing(5);
            table.setBorderWidth(2);

            table.addCell("ID Usuario");
            table.addCell("Nombre");
            table.addCell("Email");
            table.addCell("Dirección");
            table.addCell("Teléfono");
            table.addCell("Rol");
            table.addCell("Fecha Registro");

            for (UserDTO usuario : usuarios) {
                table.addCell(String.valueOf(usuario.getId()));
                table.addCell(usuario.getNombre());
                table.addCell(usuario.getEmail());
                table.addCell(usuario.getDireccion());
                table.addCell(usuario.getTelefono());
                table.addCell(usuario.getRol());
                table.addCell(usuario.getFechaRegistro());
            }

            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
