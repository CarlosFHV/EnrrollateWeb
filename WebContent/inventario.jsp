<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html lang="es">
<head>
<meta http-equiv="content-type"
	content="text/html; charset=windows-1252">
<meta name="ArachnoCoders" content="ArachnoCoders">
    <title>Gestionar Inventario</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">

</head>
<body>
    <h1>Inventario</h1>
    <form action="InventarioInsert" method="post">
        <input type="text" name="ID_Producto" placeholder="ID_Producto">
        <input type="text" name="Cantidad_Disponible" placeholder="Cantidad Disponible">
        <input type="text" name="Ubicacion" placeholder="Ubicación en Almacen">
        <button type="submit">Agregar Inventario</button>
    </form>
    <h2>Lista de Inventario</h2>
    <table border="0" width="100%">
		<tr class="form">
			<td align="center"><div class="label">ID_Inventario</div></td>
			<td align="center"><div class="label">ID_Producto</div></td>
			<td align="center"><div class="label">Cantidad_Disponible</div></td>
			<td align="center"><div class="label">Ubicación_Almacen</div></td>
			<td align="center"><div class="label">MODIFICAR</div></td>
			<td align="center"><div class="label">DETALLE</div></td>
			<td align="center"><div class="label">PDF</div></td>
			<td align="center"><div class="label">HTML</div></td>
			<td align="center"><div class="label">XLS</div></td>
			<td align="center"><div class="label">BORRAR</div></td>
		</tr>

		 <c:forEach var="inventario" items="${inventarios}"> 
			<tr>
				<form method="post"
					action="${pageContext.request.contextPath}/Inventario">
					<td align="center"><input type="hidden" name="ID_Inventario"
						value="${inventario.idInventario}">${inventario.idInventario}</td>
					<td align="center"><input type="text" name="ID_Producto"
						value="${inventario.idProducto}" size="10"></td>
					<td align="center"><input type="text"
						name="Cantidad_Disponible" value="${inventario.cantidad}" size="15"></td>
					<td align="center"><input type="text" name="Ubicacion_Almacen"
						value="${inventario.ubicacion}" size="20"></td>


					<td align="center"><input type="submit" value="Modificar"></td>
					
					</form>                                                  
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="Ver" onclick="window.location='InventarioView?llave=${inventario.idInventario}'"></td>
                            </form>
                            
                                                        <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                              <td align="center">   <input type="button" value="PDF" onclick="window.location='InventarioFormPdf?id_inventario=${inventario.idInventario}'"></td>
                            </form>
                            
                                <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                           <td align="center">      <input type="button" value="Html" onclick="window.location='InventarioFormHtml?id_inventario=${inventario.idInventario}'"></td>
                            </form>
                        
                            
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="XLS" onclick="window.location='InventarioFormXLS?id_inventario=${inventario.idInventario}'"></td>
                            </form>
                            
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="BORRAR" onclick="window.location='InventarioDelete?id_inventario=${inventario.idInventario}'"></td>
                            </form>
			</tr>
		</c:forEach> 
	
	</table>
	<div style="text-align: center; margin-top: 20px;">
    <form method="get" action="${pageContext.request.contextPath}/InventarioPDF">
        <input type="submit" value="Exportar en PDF" onclick="window.location='InventarioPDF'">
    </form>
    <form method="get" action="${pageContext.request.contextPath}/InventarioHTML">
        <input type="submit" value="Exportar en HTML" onclick="window.location='InventarioHTML'">
    </form>
    <form method="get" action="${pageContext.request.contextPath}/InventarioXLS">
        <input type="submit" value="Exportar en XLS" onclick="window.location='InventarioXLS'">
    </form>
</div>
</body>
</html>
