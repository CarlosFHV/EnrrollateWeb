<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.*" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesi�n
        
        return; // Termina la ejecuci�n
    }
%>
<%
    int idUsuario = (Integer) session.getAttribute("idUsuario"); // Aseg�rate de que el ID del usuario est� en la sesi�n
    String nombre = "", email = "", direccion = "", telefono = "", password = "";

    // Configuraci�n de la conexi�n a la base de datos
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/TestDS");

        // Abre la conexi�n
        connection = dataSource.getConnection();

        // Consulta SQL para obtener la informaci�n del usuario
        String query = "SELECT Nombre, Email, Direcci�n, Tel�fono, Password FROM usuarios WHERE ID_Usuario = ?";
        statement = connection.prepareStatement(query);
        statement.setInt(1, idUsuario);

        resultSet = statement.executeQuery();
        if (resultSet.next()) {
            nombre = resultSet.getString("Nombre");
            email = resultSet.getString("Email");
            direccion = resultSet.getString("Direcci�n");
            telefono = resultSet.getString("Tel�fono");
            password = resultSet.getString("Password");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        out.println("<p style='color:red;'>Error al acceder a la base de datos: " + e.getMessage() + "</p>");
    } catch (NamingException e) {
        e.printStackTrace();
        out.println("<p style='color:red;'>Error en la configuraci�n de la base de datos: " + e.getMessage() + "</p>");
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
    <title>Actualizar Informaci�n de Usuario</title>
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
    <h1>Actualizar Informaci�n de Usuario</h1>
    <form action="ActualizarUsuario" method="post">
        <input type="hidden" name="ID_Usuario" value="<%= idUsuario %>">
        <label>Nombre:</label>
        <input type="text" name="Nombre" value="<%= nombre %>" required><br>

        <label>Email:</label>
        <input type="email" name="Email" value="<%= email %>" required><br>

        <label>Contrase�a:</label>
        <input type="password" name="Password" value="<%= password %>" required><br>

        <label>Direcci�n:</label>
        <input type="text" name="Direcci�n" value="<%= direccion %>" required><br>

        <label>Tel�fono:</label>
        <input type="text" name="Tel�fono" value="<%= telefono %>" required><br>

        <input type="submit" value="Actualizar">
    </form>

    <% 
        // Mensaje despu�s de la actualizaci�n
        String mensaje = request.getParameter("mensaje");
        if (mensaje != null) {
    %>
        <p style='color: green;'><%= mensaje %></p>
    <% } %>
</body>
</html>
