<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.*" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<%
    int idUsuario = (Integer) session.getAttribute("idUsuario"); // Asegúrate de que el ID del usuario esté en la sesión
    String nombre = "", email = "", direccion = "", telefono = "", password = "";

    // Configuración de la conexión a la base de datos
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        // Abre la conexión
        connection = dataSource.getConnection();

        // Consulta SQL para obtener la información del usuario
        String query = "SELECT Nombre, Email, Dirección, Teléfono, Password FROM usuarios WHERE ID_Usuario = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, idUsuario);

        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            nombre = resultSet.getString("Nombre");
            email = resultSet.getString("Email");
            direccion = resultSet.getString("Dirección");
            telefono = resultSet.getString("Teléfono");
            password = resultSet.getString("Password");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<p style='color:red;'>Error al acceder a la base de datos: " + e.getMessage() + "</p>");
    } catch (NamingException e) {
        e.printStackTrace();
        out.println("<p style='color:red;'>Error en la configuración de la base de datos: " + e.getMessage() + "</p>");
    } finally {
        // Cierra los recursos
        if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
        if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
        if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Actualizar Información de Usuario</title>
       <style>
        /* Estilo para el formulario */
        form {
            display: flex;
            flex-direction: column;
            width: 300px; /* Ajusta el ancho del formulario */
            margin: 0 auto; /* Centra el formulario */
        }
        .form-group {
            display: flex;
            align-items: center; /* Alinea verticalmente el texto y el input */
            margin-bottom: 10px; /* Espaciado entre los campos */
        }
        .form-group label {
            width: 100px; /* Ancho fijo para las etiquetas */
        }
        .form-group input {
            flex: 1; /* Hace que los inputs ocupen el resto del espacio */
        }
    </style>
</head>
<body>
    <h1>Actualizar Información de Usuario</h1>
    <form action="ActualizarUsuario" method="post">
        <input type="hidden" name="ID_Usuario" value="<%= idUsuario %>">
        <label>Nombre:</label>
        <input type="text" name="Nombre" value="<%= nombre %>" required><br>

        <label>Email:</label>
        <input type="email" name="Email" value="<%= email %>" required><br>

        <label>Contraseña:</label>
        <input type="password" name="Password" value="<%= password %>" required><br>

        <label>Dirección:</label>
        <input type="text" name="Dirección" value="<%= direccion %>" required><br>

        <label>Teléfono:</label>
        <input type="text" name="Teléfono" value="<%= telefono %>" required><br>

        <input type="submit" value="Actualizar">
    </form>

    <% 
        // Mensaje después de la actualización
        String mensaje = request.getParameter("mensaje");
        if (mensaje != null) {
    %>
        <p style='color: green;'><%= mensaje %></p>
    <% } %>
</body>
</html>
