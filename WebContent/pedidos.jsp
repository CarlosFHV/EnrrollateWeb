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
    <title>Gestionar Pedidos</title>
</head>
<body>
    <h1>Pedidos</h1>
    <form action="PedidoServlet" method="post">
        <input type="text" name="id_usuario" placeholder="ID Usuario" required>
        <input type="text" name="direccion_envio" placeholder="Direcci�n de Env�o" required>
        <select name="estado">
            <option value="pendiente">Pendiente</option>
            <option value="procesado">Procesado</option>
            <option value="enviado">Enviado</option>
            <option value="entregado">Entregado</option>
            <option value="cancelado">Cancelado</option>
        </select>
        <button type="submit">Crear Pedido</button>
    </form>
    <h2>Lista de Pedidos</h2>
    <!-- Aqu� ir�a un c�digo JSP para listar pedidos desde la base de datos -->
</body>
</html>
