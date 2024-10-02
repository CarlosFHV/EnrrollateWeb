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
import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;

/**
 * Servlet implementation class ProveedorUpdate
 */
@WebServlet(name = "ProveedorUpdate", urlPatterns = { "/ProveedorUpdate"})
public class ProveedorUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProveedorUpdate() {
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response)
            throws ServletException, IOException {
            log("Actualizando Información del Proveedor");
            try {
            updateProveedor(request, response);
            
            } catch (Exception e) {
            	log("Error durante la actualización del proveedor", e);
            throw new ServletException(e);
     
            }
            String base = request.getContextPath();
            response.sendRedirect(base + "/ProveedorUpdateForm");
            }
 
 private void updateProveedor(HttpServletRequest request, HttpServletResponse response)
            throws NamingException, SQLException {
	 	String idProveedor = request.getParameter("ID_Proveedor");
	    String nombreProveedor = request.getParameter("Nombre_Proveedor");
	    String contacto = request.getParameter("Contacto");
	    String telefono = request.getParameter("Teléfono");
        String email = request.getParameter("Email");

        ProveedorDTO proveedor = new ProveedorDTO();
        proveedor.setIdProveedor(idProveedor);
        proveedor.setNombreProveedor(nombreProveedor);
        proveedor.setContacto(contacto);
        proveedor.setTelefono(telefono);
        proveedor.setEmail(email);
        
      
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        
        try {
            updateProveedor(connection, proveedor);
        } finally {
            connection.close();
        }
    }
 
 private void updateProveedor(Connection connection,ProveedorDTO proveedor) throws SQLException {
        String sql = "UPDATE proveedores SET Nombre_Proveedor = ?, Contacto = ?, Teléfono = ?, Email = ? WHERE ID_Proveedor = ?";
        PreparedStatement statement = null;
        
        try {
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, proveedor.getNombreProveedor());
            statement.setString(2, proveedor.getContacto());
            statement.setString(3, proveedor.getTelefono());
            statement.setString(4, proveedor.getEmail());
            statement.setString(5, proveedor.getIdProveedor()); 
            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

}
