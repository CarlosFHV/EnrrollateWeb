<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Administrar Pedidos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> 
</head>
<body>
    <h1>Administrar Pedidos</h1>

    <c:if test="${not empty mensaje}">
        <p>${mensaje}</p>
    </c:if>

    <c:if test="${not empty pedidos}">
        <table border="1" cellpadding="10">
            <tr>
                <th>ID Pedido</th>
                <th>Fecha de Pedido</th>
                <th>Estado</th>
                <th>Dirección de Envío</th>
                <th>Actualizar Estado</th>
            </tr>
            <c:forEach var="pedido" items="${pedidos}">
                <tr>
                    <td>${pedido.idPedido}</td>
                    <td>${pedido.fechaPedido}</td>
                    <td>${pedido.estado}</td>
                    <td>${pedido.direccionEnvio}</td>
                    <td>
                        <form action="AdminPedidosServlet" method="post">
                            <input type="hidden" name="idPedido" value="${pedido.idPedido}" />
                            <select name="estado">
                                <option value="pendiente">Pendiente</option>
                                <option value="procesado">Procesado</option>
                                <option value="enviado">Enviado</option>
                                <option value="entregado">Entregado</option>
                                <option value="cancelado">Cancelado</option>
                            </select>
                            <input type="submit" value="Actualizar" />
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <h2>Exportar Pedidos</h2>
    <div class="export-buttons">
        <form action="ExportarPedidosPdfServlet" method="get" style="display:inline;">
            <input type="submit" value="Exportar a PDF" />
        </form>
        <form action=ExportarPedidosXlsServlet method="get" style="display:inline;">
            <input type="submit" value="Exportar a XLS" />
        </form>
        <form action="ExportarPedidosHtmlServlet" method="get" style="display:inline;">
            <input type="submit" value="Exportar a HTML" />
        </form>
    </div>
    
  
</body>
</html>
