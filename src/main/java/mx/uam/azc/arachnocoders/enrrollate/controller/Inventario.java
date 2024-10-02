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

import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.UsuarioDTO;

/**
 * Servlet implementation class Inventario
 */
@WebServlet(name ="Inventario",urlPatterns = { "/Inventario" })
public class Inventario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Inventario() {
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 protected void doPost(HttpServletRequest request, HttpServletResponse
	            response)
	            throws ServletException, IOException {
	            log("Actualizando Información del Inventario");
	            try {
	            updateInventario(request, response);
	            
	            } catch (Exception e) {
	            	log("Error durante la actualización del inventario", e);
	            throw new ServletException(e);
	     
	            }
	            String base = request.getContextPath();
	            response.sendRedirect(base + "/InventarioForm");
	            }
	 
	 private void updateInventario(HttpServletRequest request, HttpServletResponse response)
	            throws NamingException, SQLException {
		    String idInventario = request.getParameter("ID_Inventario");
		    String idProducto = request.getParameter("ID_Producto");
		    String cantidad = request.getParameter("Cantidad_Disponible");
	        String ubicacionA = request.getParameter("Ubicación_Almacen");


	        InventarioDTO inventario = new InventarioDTO();
	        inventario.setIdInventario(idInventario);
	        inventario.setIdProducto(idProducto);
	        inventario.setCantidad(cantidad);
	        inventario.setUbicacion(ubicacionA);
	        
	        Context context = new InitialContext();
	        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
	        Connection connection = source.getConnection();
	        
	        try {
	            updateInventario(connection, inventario);
	        } finally {
	            connection.close();
	        }
	    }
	 
	 private void updateInventario(Connection connection,InventarioDTO inventario) throws SQLException {
	        String sql = "UPDATE inventario SET ID_Producto = ?, Cantidad_Disponible = ?, Ubicación_Almacen = ? WHERE ID_Inventario = ?";
	        PreparedStatement statement = null;
	        
	        try {
	            statement = connection.prepareStatement(sql);
	            
	            statement.setString(1, inventario.getIdProducto());
	            statement.setString(2, inventario.getCantidad());
	            statement.setString(3, inventario.getUbicacion());
	            statement.setString(4, inventario.getIdInventario());
	            statement.executeUpdate();
	        } finally {
	            if (statement != null) {
	                statement.close();
	            }
	        }
	    }

}