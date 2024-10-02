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
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.Pedido;

/**
 * Servlet implementation class MisPedidosServlet
 */
@WebServlet(name = "MisPedidosServlet", urlPatterns = { "/MisPedidosServlet" })
public class MisPedidosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Integer idUsuario = (Integer) session.getAttribute("idUsuario");
		System.out.println("ID en servlet: " + idUsuario);

		if (idUsuario == null) {
			response.sendRedirect("login.jsp");
			return;
		}


		if (!comprobarConexion()) {
			request.setAttribute("mensaje", "No se pudo conectar a la base de datos.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		try {
			List<Pedido> pedidos = obtenerPedidosPorUsuario(idUsuario);

			System.out.println("Pedidos encontrados: " + pedidos.size());
			for (Pedido pedido : pedidos) {
				System.out.println("Pedido ID: " + pedido.getIdPedido());
			}
			
			request.setAttribute("pedidos", pedidos);

			request.getRequestDispatcher("/mis_pedidos.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    Integer idUsuario = (Integer) session.getAttribute("idUsuario");

	    if (idUsuario == null) {
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    // Obtener la dirección de envío desde el formulario
	    String direccionEnvio = request.getParameter("direccionEnvio");

	    // Validar que la dirección no esté vacía
	    if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
	        request.setAttribute("mensajeError", "La dirección de envío no puede estar vacía.");
	        request.getRequestDispatcher("/ingresar_direccion.jsp").forward(request, response);
	        return;
	    }

	    try {
	        // Insertar el nuevo pedido
	        insertarNuevoPedido(idUsuario, direccionEnvio);

	        // Establecer mensaje de éxito
	        request.setAttribute("mensajeExito", "El pedido fue agregado con éxito.");

	        // Llamar al método doGet para mostrar los pedidos con el mensaje de éxito
	        doGet(request, response);
	    } catch (Exception e) {
	        throw new ServletException(e);
	    }
	}


	private List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario) throws NamingException, SQLException {
		Context context = new InitialContext();
		DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
		List<Pedido> pedidos = new ArrayList<>();

		String sql = "SELECT ID_Pedido, Fecha_Pedido, Estado, Dirección_Envio FROM Pedidos WHERE ID_Usuario = ?";

		try (Connection connection = source.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, idUsuario);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Pedido pedido = new Pedido();
					pedido.setIdPedido(resultSet.getInt("ID_Pedido"));
					pedido.setFechaPedido(resultSet.getTimestamp("Fecha_Pedido"));
					pedido.setEstado(resultSet.getString("Estado"));
					pedido.setDireccionEnvio(resultSet.getString("Dirección_Envio"));
					pedidos.add(pedido);
				}
			}
		}
		return pedidos;
	}

	private void insertarNuevoPedido(Integer idUsuario, String direccionEnvio) throws NamingException, SQLException {
	    Context context = new InitialContext();
	    DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

	    // Consulta SQL para insertar un nuevo pedido
	    String sqlPedido = "INSERT INTO Pedidos (ID_Usuario, Dirección_Envio, Estado) VALUES (?, ?, 'pendiente')";

	    // Consulta SQL para insertar un nuevo envío asociado al pedido
	    String sqlEnvio = "INSERT INTO Envios (ID_Pedido, Fecha_Envio, Fecha_Entrega, Estado_Envio, Empresa_Transporte) "
	                    + "VALUES (?, NOW(), NOW() + INTERVAL 5 DAY, 'en tránsito', 'Transportes Arachno')";

	    try (Connection connection = source.getConnection();
	         PreparedStatement statementPedido = connection.prepareStatement(sqlPedido, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        // Insertar el nuevo pedido
	        statementPedido.setInt(1, idUsuario);
	        statementPedido.setString(2, direccionEnvio);
	        int rowsInserted = statementPedido.executeUpdate();

	        if (rowsInserted > 0) {
	            System.out.println("Nuevo pedido creado para el usuario: " + idUsuario);

	            // Obtener el ID del pedido recién creado
	            try (ResultSet generatedKeys = statementPedido.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int idPedido = generatedKeys.getInt(1);

	                    // Ahora insertar el envío asociado
	                    try (PreparedStatement statementEnvio = connection.prepareStatement(sqlEnvio)) {
	                        statementEnvio.setInt(1, idPedido);
	                        statementEnvio.executeUpdate();
	                        System.out.println("Envío creado para el pedido ID: " + idPedido);
	                    }
	                }
	            }
	        }
	    }
	}

	private boolean comprobarConexion() {
		try {
			Context context = new InitialContext();
			DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

			try (Connection connection = source.getConnection()) {
				System.out.println("Conexión a la base de datos exitosa.");
				return true;
			}
		} catch (NamingException e) {
			System.out.println("Error de Naming: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Error de SQL: " + e.getMessage());
		}
		return false;
	}
}
