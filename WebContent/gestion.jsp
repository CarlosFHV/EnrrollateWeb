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
    <h1>Módulo de Gestión para Administradores</h1> <!-- Muestra el email -->
        <a style=" width: 300px;" href="UsuarioUpdateForm">Gestionar Usuarios</a><br><br>
        <a style=" width: 300px;" href="productos.jsp">Gestionar Productos</a><br><br>
        <a style=" width: 300px;" href="AdminPedidosServlet">Gestionar Pedidos</a><br><br>
        <a style=" width: 300px;" href="EnvioUpdateForm"">Gestionar Envíos</a><br><br>
        <a style=" width: 300px;" href="ProveedorUpdateForm">Gestionar Proveedores</a><br><br>
        <a style=" width: 300px;" href="InventarioForm">Gestionar Inventario</a><br><br>
     
</body>
</html>
