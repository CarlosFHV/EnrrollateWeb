<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Modificar Env�os</title>
</head>
<body>
    <h2>Modificar Env�os</h2>
    <table border="1">
        <tr>
            <th>ID Env�o</th>
            <th>ID Pedido</th>
            <th>Fecha Env�o</th>
            <th>Fecha Entrega</th>
            <th>Estado Env�o</th>
            <th>Empresa Transporte</th>
            <th>Modificar</th>
        </tr>
        <c:forEach var="envio" items="${envios}">
            <tr>
                <form method="post" action="${pageContext.request.contextPath}/EnvioUpdate">
                    <td><input type="hidden" name="id_envio" value="${envio.idEnvio}">${envio.idEnvio}</td>
                    <td>${envio.idPedido}</td>
                    <td>${envio.fechaEnvio}</td>
                    <td><input type="text" name="fecha_entrega" value="${envio.fechaEntrega}"></td>
                    <td>
                        <select name="estado_envio">
                            <option value="en tr�nsito" <c:if test="${envio.estadoEnvio == 'en tr�nsito'}">selected</c:if>>en tr�nsito</option>
                            <option value="entregado" <c:if test="${envio.estadoEnvio == 'entregado'}">selected</c:if>>entregado</option>
                            <option value="retrasado" <c:if test="${envio.estadoEnvio == 'retrasado'}">selected</c:if>>retrasado</option>
                        </select>
                    </td>
                    <td><input type="text" name="empresa_transporte" value="${envio.empresaTransporte}"></td>
                    <td><input type="submit" value="Modificar"></td>
                </form>
            </tr>
        </c:forEach>
    </table>
<div>
    <h3>Exportar Registros</h3>

    <form method="get" action="${pageContext.request.contextPath}/ExportarEnviosXls">
        <input type="submit" value="Exportar a XLS">
    </form>

    <form method="get" action="${pageContext.request.contextPath}/ExportarEnviosPdfServlet">
        <input type="submit" value="Exportar a PDF">
    </form>

    <form method="get" action="${pageContext.request.contextPath}/ExportarEnviosHtmlServlet">
        <input type="submit" value="Exportar a HTML">
    </form>
</div>

</body>
</html>
