package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "EnvioUpdate", urlPatterns = { "/EnvioUpdate" })
public class EnvioUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            updateEnvio(request);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        response.sendRedirect(request.getContextPath() + "/EnvioUpdateForm");
    }

    private void updateEnvio(HttpServletRequest request) throws NamingException, SQLException {
        int idEnvio = Integer.parseInt(request.getParameter("id_envio"));
        String fechaEntrega = request.getParameter("fecha_entrega");
        String estadoEnvio = request.getParameter("estado_envio");
        String empresaTransporte = request.getParameter("empresa_transporte");

        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        // Verificar si la fecha de entrega está vacía y establecer como null si es necesario
        if (fechaEntrega == null || fechaEntrega.trim().isEmpty()) {
            fechaEntrega = null; // o puedes manejarlo de otra manera según tu lógica
        }
        try {
            String sql = "UPDATE Envios SET Fecha_Entrega = ?, Estado_Envio = ?, Empresa_Transporte = ? WHERE ID_Envio = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fechaEntrega);
            statement.setString(2, estadoEnvio);
            statement.setString(3, empresaTransporte);
            statement.setInt(4, idEnvio);
            statement.executeUpdate();
        } finally {
            connection.close();
        }
    }
}
