package mx.uam.azc.arachnocoders.enrrollate.controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.InventarioDTO;
import mx.uam.azc.arachnocoders.enrrollate.data.UsuarioDTO;

/**
 * Servlet implementation class InventarioForm
 */
@WebServlet(name = "InventarioForm", urlPatterns = { "/InventarioForm" })
public class InventarioForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InventarioForm() {
      
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
        	log("Enviando inventario");
            List<InventarioDTO> Inventario = getInventario(); 
            request.setAttribute("inventarios", Inventario);
        } catch (Exception e) {
        	log("Error al enviar inventario");
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/inventario.jsp");
        dispatcher.forward(request, response);

    }
    
    private List<InventarioDTO> getInventario() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        try {
            return getInventario(connection);
        } finally {
            connection.close();
        }
    }

    private List<InventarioDTO> getInventario(Connection connection) throws
    SQLException {
        Statement statement = connection.createStatement();
        ResultSet cursor = statement.executeQuery("SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario");
        try {
            List<InventarioDTO> inventarios = new ArrayList<InventarioDTO>();
            while (cursor.next()) {
                InventarioDTO inventario = new InventarioDTO();
                
                inventario.setIdInventario(cursor.getString(1));
                inventario.setIdProducto(cursor.getString(2));
                inventario.setCantidad(cursor.getString(3));
                inventario.setUbicacion(cursor.getString(4));
  
                inventarios.add(inventario);
            }
            return inventarios;
        } finally {
        cursor.close();
        }
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

}