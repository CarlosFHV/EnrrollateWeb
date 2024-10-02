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

import mx.uam.azc.arachnocoders.enrrollate.data.Pedido;

@WebServlet(name = "AdminPedidosServlet", urlPatterns = { "/AdminPedidosServlet" })
public class AdminPedidosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Pedido> pedidos = obtenerTodosLosPedidos();
            request.setAttribute("pedidos", pedidos);
            request.getRequestDispatcher("/admin_pedidos.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<Pedido> obtenerTodosLosPedidos() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        List<Pedido> pedidos = new ArrayList<>();

        String sql = "SELECT ID_Pedido, Fecha_Pedido, Estado, Dirección_Envio FROM Pedidos";

        try (Connection connection = source.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(resultSet.getInt("ID_Pedido"));
                pedido.setFechaPedido(resultSet.getTimestamp("Fecha_Pedido"));
                pedido.setEstado(resultSet.getString("Estado"));
                pedido.setDireccionEnvio(resultSet.getString("Dirección_Envio"));
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idPedido = Integer.parseInt(request.getParameter("idPedido"));
        String nuevoEstado = request.getParameter("estado");

        // Verificar que el nuevo estado no sea null
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            request.setAttribute("mensajeError", "El estado no puede ser nulo o vacío.");
            doGet(request, response);
            return;
        }

        try {
            Context context = new InitialContext();
            DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            try (Connection connection = source.getConnection()) {
                String sql = "UPDATE Pedidos SET Estado = ? WHERE ID_Pedido = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nuevoEstado);
                preparedStatement.setInt(2, idPedido);
                int filasAfectadas = preparedStatement.executeUpdate();

                if (filasAfectadas > 0) {
                    request.setAttribute("mensaje", "Estado actualizado con éxito.");
                    System.out.println("Actualización exitosa");
                } else {
                    request.setAttribute("mensajeError", "No se encontró el pedido con ID: " + idPedido);
                    System.out.println("Actualización fallida");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos: " + e.getMessage());
        } catch (NamingException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de conexión: " + e.getMessage());
        }

        doGet(request, response);
    }


    private void actualizarEstadoPedido(Integer idPedido, String nuevoEstado) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        String sql = "UPDATE Pedidos SET Estado = ? WHERE ID_Pedido = ?";

        try (Connection connection = source.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nuevoEstado);
            statement.setInt(2, idPedido);
            statement.executeUpdate();
        }
    }
}
