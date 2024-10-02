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
<title>Detalle de Proveedor</title>
<link rel="stylesheet"
    href="${ pageContext.request.contextPath }/css/style.css">
</head>
<body>
    <form method="get"
        action="${ pageContext.request.contextPath }/proveedor_view.jsp">
        <div class="step">Detalle del Proveedor/div>
        <br><br>
        <table border="0" width="140%">
            <tr class="form">
                <td align="center">
                    <div class="label">ID Proveedor</div>
                </td>
                <td align="center">
                    <div class="label">Nombre Proveedor</div>
                </td>
                <td align="center">
                    <div class="label">Contacto</div>
                </td>
                <td align="center">
                    <div class="label">Teléfono</div>
                </td>
                <td align="center">
                    <div class="label">Email</div>
                </td>
               
            </tr>
            <c:forEach var="proveedor" items="${ proveedores }">
                <tr>
                    <td align="center">${ proveedor.idProveedor }</td>
                    <td align="center">${ proveedor.nombreProveedor }</td>
                    <td align="center">${ proveedor.contacto }</td>
                    <td align="center">${ proveedor.telefono }</td>
                    <td align="center">${ proveedor.email }</td>
                </tr>
            </c:forEach>
        </table>
        <br> 
        <input type="button" value=" Regresar "
            onclick="window.location='${ pageContext.request.contextPath }/ProveedorUpdateForm'">
    </form>
    <br>
</body>