<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
<meta http-equiv="content-type"
	content="text/html; charset=windows-1252">
<meta name="ArachnoCoders" content="ArachnoCoders">
<title>Forma de Búsqueda de Usuarios</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<!-- Formulario de búsqueda -->
	<form method="get"
		action="${pageContext.request.contextPath}/usuarios.jsp">
		<div class="step">Forma de Búsqueda de Usuario</div>
		<div class="instructions">Proporciona la información de búsqueda
			solicitada</div>
		<br>
		<c:set var="pattern" value="${param.pattern}" />
		<c:if test="${param.pattern == null || pattern == ''}">
			<c:set var="pattern" value="%" />
		</c:if>
		<table border="1" cellpadding="10">
			<tr>
				<td align="center">
					<table>
						<tr class="form">
							<td align="right">
								<div class="label">Patrón:</div>
							</td>
							<td><input name="pattern" size="10" value="${pattern}"></td>
							<td><input type="submit" value="Buscar"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
	<br>

	<!-- Tabla de resultados -->
	<table border="0" width="100%">
		<tr class="form">
			<td align="center"><div class="label">ID</div></td>
			<td align="center"><div class="label">NOMBRE</div></td>
			<td align="center"><div class="label">EMAIL</div></td>
			<td align="center"><div class="label">DIRECCIÓN</div></td>
			<td align="center"><div class="label">TELÉFONO</div></td>
			<td align="center"><div class="label">ROL</div></td>
			<td align="center"><div class="label">FECHA_REGISTRO</div></td>
			<td align="center"><div class="label">MODIFICAR</div></td>
			<td align="center"><div class="label">DETALLE</div></td>
			<td align="center"><div class="label">PDF</div></td>
			<td align="center"><div class="label">HTML</div></td>
			<td align="center"><div class="label">XLS</div></td>
		</tr>
	
		<c:forEach var="usuario" items="${usuarios}">
			<tr>
				<form method="post"
					action="${pageContext.request.contextPath}/UsuarioUpdate">
					<td align="center"><input type="hidden" name="id_usuario"
						value="${usuario.idUsuario}"> ${usuario.idUsuario}</td>
					<td align="center"><input type="text" name="nombre"
						value="${usuario.nombre}" size="10"></td>
					<td align="center"><input type="text" name="email"
						value="${usuario.email}" size="15"></td>
					<td align="center"><input type="text" name="dirección"
						value="${usuario.direccion}" size="20"></td>
					<td align="center"><input type="text" name="teléfono"
						value="${usuario.telefono}" size="10"></td>
					<td align="center"><input type="text" name="rol"
						value="${usuario.rol}" size="10"></td>
					<td align="center"><input type="text" name="fecha_registro"
						value="${usuario.fechaRegistro}" size="15"></td>
                        
                            
                             <td align="center">  <input type="submit" value="Modificar" ></td>
                            </form>                                                  
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="Ver" onclick="window.location='UserForm?llave=${usuario.idUsuario}'"></td>
                            </form>
                            
                                                        <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                              <td align="center">   <input type="button" value="PDF" onclick="window.location='UserFormPdf?id_usuario=${usuario.idUsuario}'"></td>
                            </form>
                            
                                <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                           <td align="center">      <input type="button" value="Html" onclick="window.location='UserFormHtml?id_usuario=${usuario.idUsuario}'"></td>
                            </form>
                        
                            
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${usuario.idUsuario}">
                               <td align="center">  <input type="button" value="XLS" onclick="window.location='UserFormXls?id_usuario=${usuario.idUsuario}'"></td>
                            </form>

                       
				
			</tr>
		</c:forEach>
	</table>
	<br>
	<!-- Botón Regresar -->
	<input type="button" value="Regresar"
		onclick="window.location='${pageContext.request.contextPath}/main.jsp'">
</body>
</html>
