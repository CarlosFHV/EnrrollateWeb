package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
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

import mx.uam.azc.arachnocoders.enrrollate.data.EnvioDTO;

@WebServlet(name = "MisEnviosServlet", urlPatterns = { "/MisEnviosServlet" })
public class MisEnviosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");

        if (idPedidoParam == null || idPedidoParam.isEmpty()) {
            request.setAttribute("mensajeError", "ID de pedido no válido.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int idPedido = Integer.parseInt(idPedidoParam);
        try {
            List<EnvioDTO> envios = obtenerEnviosPorIdPedido(idPedido);
            request.setAttribute("envios", envios);
            request.getRequestDispatcher("/mis_envios.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<EnvioDTO> obtenerEnviosPorIdPedido(int idPedido) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        List<EnvioDTO> envios = new ArrayList<>();

        String sql = "SELECT ID_Envio, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte FROM Envios WHERE ID_Pedido = ?";

        try (Connection connection = source.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idPedido);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EnvioDTO envio = new EnvioDTO();
                    envio.setIdEnvio(resultSet.getInt("ID_Envio"));
                    envio.setFechaEnvio(resultSet.getTimestamp("Fecha_Envio"));
                    envio.setFechaEntrega(resultSet.getTimestamp("Fecha_Entrega"));
                    envio.setEstadoEnvio(resultSet.getString("Estado_Envio"));
                    envio.setEmpresaTransporte(resultSet.getString("Empresa_Transporte"));
                    envios.add(envio);
                }
            }
        }
        return envios;
    }
}
