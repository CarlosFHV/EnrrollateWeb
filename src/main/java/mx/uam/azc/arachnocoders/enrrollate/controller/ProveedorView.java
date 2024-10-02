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
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;

/**
 * Servlet implementation class ProveedorView
 */
@WebServlet("/ProveedorView")
public class ProveedorView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProveedorView() {
      
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int key = Integer.parseInt(request.getParameter("llave"));
        try {
            List<ProveedorDTO> proveedores = getProveedor(key);

            request.setAttribute("proveedores", proveedores); // Cambiar el atributo a "usuarios"
        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/proveedor_view.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Obtener los usuarios registrados en la base de datos.
     */
    private List<ProveedorDTO> getProveedor(int key) throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        Connection connection = source.getConnection();

        try {
            return getProveedor(connection, key);
        } finally {
            connection.close();
        }
    }

    /**
     * Realizar la consulta sobre la base de datos, y regresar la lista de usuarios.
     */
    private List<ProveedorDTO> getProveedor(Connection connection, int key) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores"
        			+ " WHERE ID_Proveedor = " + key ;
        		
        ResultSet cursor = statement.executeQuery(query);

        List<ProveedorDTO> proveedores = new ArrayList<ProveedorDTO>();
        try {
        	while (cursor.next()) {
        		ProveedorDTO proveedor = new ProveedorDTO();
                
                proveedor.setIdProveedor(cursor.getString(1));
                proveedor.setNombreProveedor(cursor.getString(2));
                proveedor.setContacto(cursor.getString(3));
                proveedor.setTelefono(cursor.getString(4));
                proveedor.setEmail(cursor.getString(5));
  
                proveedores.add(proveedor);
            }
            return proveedores;
        } finally {
            cursor.close();
        }
    }
}
