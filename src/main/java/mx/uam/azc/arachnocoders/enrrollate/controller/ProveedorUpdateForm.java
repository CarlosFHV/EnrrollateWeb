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

import mx.uam.azc.arachnocoders.enrrollate.data.ProveedorDTO;

/**
 * Servlet implementation class ProveedorUpdateForm
 */
@WebServlet(name = "ProveedorUpdateForm", urlPatterns = { "/ProveedorUpdateForm" })
public class ProveedorUpdateForm extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProveedorUpdateForm() {
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
        	log("Enviando Proveedor");
            List<ProveedorDTO> Proveedor = getProveedor(); 
            request.setAttribute("proveedores", Proveedor);
        } catch (Exception e) {
        	log("Error al enviar inventario");
            throw new ServletException(e);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/proveedores.jsp");
        dispatcher.forward(request, response);

    }
    
    private List<ProveedorDTO> getProveedor() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        try {
            return getProveedor(connection);
        } finally {
            connection.close();
        }
    }

    private List<ProveedorDTO> getProveedor(Connection connection) throws
    SQLException {
        Statement statement = connection.createStatement();
        ResultSet cursor = statement.executeQuery("SELECT ID_Proveedor, Nombre_Proveedor, Contacto, Teléfono, Email FROM proveedores");
        try {
            List<ProveedorDTO> proveedores = new ArrayList<ProveedorDTO>();
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
