<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
<meta http-equiv="content-type"
	content="text/html; charset=windows-1252">
<meta name="ArachnoCoders" content="ArachnoCoders">
<title>Gestión de Productos</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<!-- Tabla de resultados -->
	<table border="0" width="100%">
		<tr class="form">
			<td align="center"><div class="label">ID</div></td>
			<td align="center"><div class="label">NOMBRE</div></td>
			<td align="center"><div class="label">DESCRIPCIÓN</div></td>
			<td align="center"><div class="label">PRECIO</div></td>
			<td align="center"><div class="label">STOCK</div></td>
			<td align="center"><div class="label">MODIFICAR</div></td>
		</tr>

		<c:forEach var="producto" items="${productos}">
			<tr>
				<form method="post"
					action="${pageContext.request.contextPath}/ActualizarProductoServlet">
					<td align="center"><input type="hidden" name="id"
						value="${producto.idProducto}"> ${producto.idProducto}</td>
					<!-- Cambiado de id_producto a id -->
					<td align="center"><input type="text" name="nombre"
						value="${producto.nombreProducto}" size="10"></td>
					<td align="center"><input type="text" name="descripcion"
						value="${producto.descripcion}" size="20"></td>
					<td align="center"><input type="text" name="precio"
						value="${producto.precio}" size="10"></td>
					<td align="center"><input type="text" name="stock"
						value="${producto.stock}" size="10"></td>

					<td align="center"><input type="submit" value="Modificar"></td>
				</form>
			</tr>
		</c:forEach>
	</table>
	<div>
		<h3>Exportar Registros</h3>
		<form method="get"
			action="${pageContext.request.contextPath}/ExportarProductosXlsServlet">
			<input type="submit" value="Exportar a XLS">
		</form>
		<form method="get"
			action="${pageContext.request.contextPath}/ExportarProductosPdfServlet">
			<input type="submit" value="Exportar a PDF">
		</form>
		<form method="get"
			action="${pageContext.request.contextPath}/ExportarProductosHtmlServlet">
			<input type="submit" value="Exportar a HTML">
		</form>
	<input type="button" value="Regresar"
		onclick="window.location='${pageContext.request.contextPath}/gestion.jsp'">
	</div>

</body>
</html>
