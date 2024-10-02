<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Mis Pedidos</title>
</head>
<body>
    <h1>Mis Pedidos</h1>

    <c:if test="${not empty mensajeExito}">
        <div style="color: green; font-weight: bold;">
            ${mensajeExito}
        </div>
    </c:if>

    <c:if test="${not empty mensajeError}">
        <div style="color: red; font-weight: bold;">
            ${mensajeError}
        </div>
    </c:if>
    <c:if test="${empty pedidos}">
        <div style="color: #555; font-style: italic;">
            No hay pedidos para este usuario.
        </div>
    </c:if>
    <c:if test="${not empty pedidos}">
        <table border="1">
            <tr>
                <th>ID Pedido</th>
                <th>Fecha de Pedido</th>
                <th>Estado</th>
                <th>Dirección de Envío</th>
                <th>Acciones</th>
            </tr>
            <c:forEach var="pedido" items="${pedidos}">
                <tr>
                    <td>${pedido.idPedido}</td>
                    <td>${pedido.fechaPedido}</td>
                    <td>${pedido.estado}</td>
                    <td>${pedido.direccionEnvio}</td>
                    <td>
                        <form action="MisEnviosServlet" method="post" style="display:inline;">
                            <input type="hidden" name="idPedido" value="${pedido.idPedido}">
                            <input type="submit" value="Ver Envío">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
	<br>
</body>
</html>
