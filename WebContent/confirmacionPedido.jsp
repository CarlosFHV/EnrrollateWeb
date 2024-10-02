<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<html>
<head>
    <title>Confirmación de Pedido</title>
</head>
<body>
    <h1>Pedido Realizado con Éxito</h1>
    <p>Su pedido ha sido procesado y se encuentra en estado <strong>pendiente</strong>.</p>
    <p>Gracias por su compra!</p>
    <p><a href="verCarrito">Volver al Carrito</a></p>
    <p><a href="VerProductos">Volver a la Página Principal</a></p>
</body>
</html>
