package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.EnvioDTO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "EnvioUpdateForm", urlPatterns = { "/EnvioUpdateForm" })
public class EnvioUpdateFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<EnvioDTO> envios = getEnvios();
            request.setAttribute("envios", envios);
        } catch (Exception e) {
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/envios.jsp");
        dispatcher.forward(request, response);
    }

    private List<EnvioDTO> getEnvios() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        try {
            return getEnvios(connection);
        } finally {
            connection.close();
        }
    }

    private List<EnvioDTO> getEnvios(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet cursor = statement.executeQuery("SELECT ID_Envio, ID_Pedido, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte FROM Envios");
        try {
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
        } finally {
            cursor.close();
        }
    }
}
