package mx.uam.azc.arachnocoders.enrrollate.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import mx.uam.azc.arachnocoders.enrrollate.data.UsuarioDTO;

/**
 * Servlet implementation class UsuarioUpdateServlet
 */
/**
 * @author ArachnoCoders
 */
@WebServlet(name = "UsuarioUpdate", urlPatterns = { "/UsuarioUpdate" })
public class UsuarioUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public UsuarioUpdateServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response)
            throws ServletException, IOException {
            log("Actualizando Información del Usuario");
            try {
            updateUsuario(request, response);
            } catch (Exception e) {
            throw new ServletException(e);
            }
            String base = request.getContextPath();
            response.sendRedirect(base + "/UsuarioUpdateForm");
            }


    /**
     * @param request
     * @param response
     * @throws NamingException
     * @throws SQLException
     */
    private void updateUsuario(HttpServletRequest request, HttpServletResponse response)
            throws NamingException, SQLException {
        String idUsuario = request.getParameter("id_usuario");
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String direccion = request.getParameter("dirección");
        String telefono = request.getParameter("teléfono");
        String rol = request.getParameter("rol");
        String fechaRegistro = request.getParameter("fecha_registro");

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setDireccion(direccion);
        usuario.setTelefono(telefono);
        usuario.setRol(rol);
        usuario.setFechaRegistro(fechaRegistro);
        
        
        Context context = new InitialContext();
        DataSource source = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");
        Connection connection = source.getConnection();
        
        try {
            updateUsuario(connection, usuario);
        } finally {
            connection.close();
        }
    }

    /**
     * 
     * @param connection    Conexión a la base de datos.
     * @param idUsuario     ID del usuario.
     * @param nombre        Nombre del usuario.
     * @param email         Email del usuario.
     * @param direccion     Dirección del usuario.
     * @param telefono      Teléfono del usuario.
     * @param rol           Rol del usuario.
     * @param fechaRegistro Fecha de registro del usuario.
     */
    private void updateUsuario(Connection connection, UsuarioDTO usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, dirección = ?, teléfono = ?, rol = ?, fecha_registro = ? WHERE id_usuario = ?";
        PreparedStatement statement = null;
        
        try {
            statement = connection.prepareStatement(sql);
            
            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getDireccion());
            statement.setString(4, usuario.getTelefono());
            statement.setString(5, usuario.getRol());
            statement.setString(6, usuario.getFechaRegistro());
            statement.setString(7, usuario.getIdUsuario());

            statement.executeUpdate();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }


}
