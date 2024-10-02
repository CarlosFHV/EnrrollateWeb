package mx.uam.azc.arachnocoders.enrrollate.controller;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;  // Importar Statement

@WebServlet(name = "RealizarPedidoServlet", urlPatterns = {"/realizarPedido"})
public class RealizarPedidoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener la dirección de envío del formulario
        String direccionEnvio = request.getParameter("direccionEnvio");
        
        // Obtener el ID del usuario desde la sesión
        HttpSession session = request.getSession();
        int idUsuario = (int) session.getAttribute("idUsuario");

        Connection connection = null;
        PreparedStatement insertPedidoStmt = null;
        PreparedStatement insertDetalleStmt = null;
        PreparedStatement insertEnvioStmt = null;

        try {
            // Establecer conexión con la base de datos a través del DataSource
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); // Comenzar transacción

            // Insertar el nuevo pedido en la base de datos
            String insertPedidoQuery = "INSERT INTO pedidos (ID_Usuario, Fecha_Pedido, Estado, Dirección_Envio) "
                                   + "VALUES (?, CURRENT_TIMESTAMP, 'pendiente', ?)";
            insertPedidoStmt = connection.prepareStatement(insertPedidoQuery, Statement.RETURN_GENERATED_KEYS);
            insertPedidoStmt.setInt(1, idUsuario);
            insertPedidoStmt.setString(2, direccionEnvio);
            insertPedidoStmt.executeUpdate();

            // Obtener el ID del nuevo pedido
            ResultSet generatedKeys = insertPedidoStmt.getGeneratedKeys();
            int idPedido = 0;
            if (generatedKeys.next()) {
                idPedido = generatedKeys.getInt(1);
            }

            // Insertar detalles del pedido desde el carrito
            String insertDetalleQuery = "INSERT INTO detalle_pedido (ID_Pedido, ID_Producto, Cantidad, Precio_Unitario) "
                                       + "SELECT ?, cp.ID_Producto, cp.Cantidad, p.Precio "
                                       + "FROM carrito_producto cp "
                                       + "JOIN productos p ON cp.ID_Producto = p.ID_Producto "
                                       + "WHERE cp.ID_Carrito IN (SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?)";
            insertDetalleStmt = connection.prepareStatement(insertDetalleQuery);
            insertDetalleStmt.setInt(1, idPedido);
            insertDetalleStmt.setInt(2, idUsuario);
            insertDetalleStmt.executeUpdate();
            
            
            
            // Insertar en la tabla de envíos
            String insertEnvioQuery = "INSERT INTO envios (ID_Pedido) VALUES (?)";
            insertEnvioStmt = connection.prepareStatement(insertEnvioQuery);
            insertEnvioStmt.setInt(1, idPedido);
            insertEnvioStmt.executeUpdate();

            // Eliminar el carrito actual
            String deleteCarritoQuery = "DELETE FROM carrito_producto WHERE ID_Carrito IN (SELECT ID_Carrito FROM carrito WHERE ID_Usuario = ?)";
            PreparedStatement deleteCarritoStmt = connection.prepareStatement(deleteCarritoQuery);
            deleteCarritoStmt.setInt(1, idUsuario);
            deleteCarritoStmt.executeUpdate();

            connection.commit();

            // Redirigir a una página de confirmación o al carrito
            response.sendRedirect("confirmacionPedido.jsp");
        } catch (NamingException | SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    throw new ServletException(ex);
                }
            }
            throw new ServletException(e);
        } finally {
            // Cerrar recursos
            try {
                if (insertPedidoStmt != null) insertPedidoStmt.close();
                if (insertDetalleStmt != null) insertDetalleStmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
    }
}
