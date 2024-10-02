<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Portal de Envíos y Pedidos</title>
</head>
<body>
    <h1>Bienvenid@, <%= session.getAttribute("email") %>!</h1> <!-- Muestra el email -->
    <ul>
        <li><a href="UsuarioUpdateForm">Gestionar Usuarios</a></li>
        <li><a href="productos.jsp">Gestionar Productos</a></li>
        <li><a href="pedidos.jsp">Gestionar Pedidos</a></li>
        <li><a href="envios.jsp">Gestionar Envíos</a></li>
        <li><a href="proveedores.jsp">Gestionar Proveedores</a></li>
        <li><a href="inventario.jsp">Gestionar Inventario</a></li>
    </ul>
    <a href="Logout">Cerrar Sesión</a> <!-- Enlace para cerrar sesión -->
</body>
</html>
