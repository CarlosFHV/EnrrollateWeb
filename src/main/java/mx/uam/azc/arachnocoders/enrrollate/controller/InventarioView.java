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
import mx.uam.azc.arachnocoders.enrrollate.data.UserDTO;

/**
 * Servlet implementation class InventarioView
 */
@WebServlet("/InventarioView")
public class InventarioView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InventarioView() {
       
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int key = Integer.parseInt(request.getParameter("llave"));
        try {
            List<InventarioDTO> inventarios = getInventario(key);

            request.setAttribute("inventarios", inventarios); // Cambiar el atributo a "usuarios"
        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/inventario_view.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Obtener los usuarios registrados en la base de datos.
     */
    private List<InventarioDTO> getInventario(int key) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        Connection connection = source.getConnection();

        try {
            return getInventario(connection, key);
        } finally {
            connection.close();
        }
    }

    /**
     * Realizar la consulta sobre la base de datos, y regresar la lista de usuarios.
     */
    private List<InventarioDTO> getInventario(Connection connection, int key) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT ID_Inventario, ID_Producto, Cantidad_Disponible, Ubicación_Almacen FROM inventario"
        			+ " WHERE ID_Inventario = " + key ;
        		
        ResultSet cursor = statement.executeQuery(query);

        List<InventarioDTO> inventarios = new ArrayList<InventarioDTO>();
        try {
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

}