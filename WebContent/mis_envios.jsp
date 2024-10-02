
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
    <title>Detalles del Envío</title>
</head>
<body>
    <h1>Detalles del Envío</h1>

    <c:if test="${not empty mensajeError}">
        <div style="color:red; font-weight: bold;">
            ${mensajeError}
        </div>
    </c:if>

    <c:if test="${empty envios}">
        <div style="color: #555; font-style: italic;">
            No hay envíos para este pedido.
        </div>
    </c:if>

    <c:if test="${not empty envios}">
        <table border="1" style="border-collapse: collapse; width: 100%; margin-top: 20px;">
            <tr>
                <th>ID Envío</th>
                <th>Fecha Envío</th>
                <th>Fecha Entrega</th>
                <th>Estado Envío</th>
                <th>Empresa Transporte</th>
            </tr>
            <c:forEach var="envio" items="${envios}">
                <tr>
                    <td>${envio.idEnvio}</td>
                    <td>${envio.fechaEnvio}</td>
                    <td>${envio.fechaEntrega}</td>
                    <td>${envio.estadoEnvio}</td>
                    <td>${envio.empresaTransporte}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <a href="MisPedidosServlet">Regresar a mis pedidos</a>
</body>
</html>
