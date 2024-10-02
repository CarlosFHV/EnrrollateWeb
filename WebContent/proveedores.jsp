<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Proveedores</title>
</head>
<body>
    <h1>Proveedores</h1>
    <form action="ProveedorInsert" method="post">
        <input type="text" name="nombre_proveedor" placeholder="Nombre del Proveedor" required>
        <input type="text" name="contacto" placeholder="Contacto">
        <input type="text" name="telefono" placeholder="Teléfono">
        <input type="email" name="email" placeholder="Email">
        <button type="submit">Agregar Proveedor</button>
    </form>
    <h2>Lista de Proveedores</h2>

    <table border="0" width="100%">
		<tr class="form">
			<td align="center"><div class="label">ID_Proveedor</div></td>
			<td align="center"><div class="label">Nombre_Proveedor</div></td>
			<td align="center"><div class="label">Contacto</div></td>
			<td align="center"><div class="label">Teléfono</div></td>
			<td align="center"><div class="label">Email</td>
			<td align="center"><div class="label">MODIFICAR</div></td>
			<td align="center"><div class="label">DETALLE</div></td>
			<td align="center"><div class="label">PDF</div></td>
			<td align="center"><div class="label">HTML</div></td>
			<td align="center"><div class="label">XLS</div></td>
			<td align="center"><div class="label">BORRAR</div></td>
		</tr>
		<c:forEach var="proveedor" items="${proveedores}"> 
			<tr>
				<form method="post"
					action="${pageContext.request.contextPath}/ProveedorUpdate">
					<td align="center"><input type="hidden" name="ID_Proveedor"
						value="${proveedor.idProveedor}">${proveedor.idProveedor}</td>
						
					<td align="center"><input type="text" name="Nombre_Proveedor"
						value="${proveedor.nombreProveedor}" size="10"></td>
						
					<td align="center"><input type="text" name="Contacto"
						value="${proveedor.contacto}" size="10"></td>
						
					<td align="center"><input type="text" name="Teléfono" 
					value="${proveedor.telefono}" size="15"></td>
						
					<td align="center"><input type="text" name="Email"
						value="${proveedor.email}" size="50"></td>


					<td align="center"><input type="submit" value="Modificar"></td>
					
					</form>                                                  
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="Ver" onclick="window.location='ProveedorView?llave=${proveedor.idProveedor}'"></td>
                            </form>
                            
                                                        <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                              <td align="center">   <input type="button" value="PDF" onclick="window.location='ProveedorFormPdf?id_proveedor=${proveedor.idProveedor}'"></td>
                            </form>
                            
                                <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                           <td align="center">      <input type="button" value="Html" onclick="window.location='ProveedorFormHtml?id_proveedor=${proveedor.idProveedor}'"></td>
                            </form>
                        
                            
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="XLS" onclick="window.location='ProveedorFromXLS?id_proveedor=${proveedor.idProveedor}'"></td>
                            </form>
                            
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="BORRAR" onclick="window.location='ProveedorDelete?id_proveedor=${proveedor.idProveedor}'"></td>
                            </form>
			</tr>
		</c:forEach> 
	</table>
    
</body>
</html>