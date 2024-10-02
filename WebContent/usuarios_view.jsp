<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    if (session == null || session.getAttribute("email") == null) { // Cambia a email
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
<meta http-equiv="content-type"
    content="text/html; charset=windows-1252">
<meta name="GENERATOR"
    content="SeaMonkey/2.40 [en] (Windows; 10; Intel(R) Core(TM) i7-4500U CPU @ 1.80GHz 2.40 GHz) [Composer]">
<meta name="Author" content="Hugo Pablo Leyva">
<title>Detalle de Usuario</title>
<link rel="stylesheet"
    href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
    <form method="get"
        action="${ pageContext.request.contextPath }/usuarios_view.jsp">
        <div class="step">Detalle del Usuario</div>
        <br><br>
        <table border="0" width="140%">
            <tr class="form">
                <td align="center">
                    <div class="label">ID</div>
                </td>
                <td align="center">
                    <div class="label">Nombre</div>
                </td>
                <td align="center">
                    <div class="label">Email</div>
                </td>
                <td align="center">
                    <div class="label">Dirección</div>
                </td>
                <td align="center">
                    <div class="label">Teléfono</div>
                </td>
                <td align="center">
                    <div class="label">Rol</div>
                </td>
                <td align="center">
                    <div class="label">Fecha de Registro</div>
                </td>
            </tr>
            <c:forEach var="usuario" items="${ usuarios }">
                <tr>
                    <td align="center">${ usuario.id }</td>
                    <td align="center">${ usuario.nombre }</td>
                    <td align="center">${ usuario.email }</td>
                    <td align="center">${ usuario.direccion }</td>
                    <td align="center">${ usuario.telefono }</td>
                    <td align="center">${ usuario.rol }</td>
                    <td align="center">${ usuario.fechaRegistro }</td>
                </tr>
            </c:forEach>
        </table>
        <br> 
        <input type="button" value=" Regresar "
            onclick="window.location='${ pageContext.request.contextPath }/UsuarioUpdateForm'">
    </form>
    <br>
</body>
</html>
