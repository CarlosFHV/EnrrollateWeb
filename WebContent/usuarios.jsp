<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="ArachnoCoders" content="ArachnoCoders">
    <title>Forma de Búsqueda de Usuarios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .action-buttons {
            display: flex;
            gap: 5px; /* Espacio entre botones */
            justify-content: center;
        }
        .action-buttons form {
            margin: 0;
        }
        .action-buttons input {
            padding: 5px 10px;
            font-size: 14px;
            width: 70px; /* Ajusta el ancho según sea necesario */
            text-align: center;
        }
    </style>
</head>
<body>
    <form method="get" action="${pageContext.request.contextPath}/usuarios.jsp">
        <div class="step">Forma de Búsqueda de Usuario</div>
        <div class="instructions">Proporciona la información de búsqueda solicitada</div>
        <br>
        <c:set var="pattern" value="${param.pattern}" />
        <c:if test="${param.pattern == null || param.pattern == ''}">
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
        <br>
        <table border="0" width="100%">
            <tr class="form">
                <td align="center"><div class="label">ID</div></td>
                <td align="center"><div class="label">NOMBRE</div></td>
                <td align="center"><div class="label">EMAIL</div></td>
                <td align="center"><div class="label">DIRECCIÓN</div></td>
                <td align="center"><div class="label">TELÉFONO</div></td>
                <td align="center"><div class="label">ROL</div></td>
                <td align="center"><div class="label">FECHA REGISTRO</div></td>
                <td align="center"><div class="label">ACCIONES</div></td>
            </tr>
            <sql:query var="resultados" dataSource="jdbc/TestDS">
                SELECT * FROM usuarios WHERE nombre LIKE ? ORDER BY nombre;
                <sql:param value="${pattern}" />
            </sql:query>
            <c:forEach var="fila" items="${resultados.rows}">
            <tr>
                <form method="post" action="${pageContext.request.contextPath}/UsuarioUpdate">
                    <td align="center"><input type="hidden" name="id_usuario" value="${fila.id_usuario}"> ${fila.id_usuario}</td>
                    <td align="center"><input type="text" name="nombre" value="${fila.nombre}" size="10"></td>
                    <td align="center"><input type="text" name="email" value="${fila.email}" size="15"></td>
                    <td align="center"><input type="text" name="dirección" value="${fila.dirección}" size="20"></td>
                    <td align="center"><input type="text" name="teléfono" value="${fila.teléfono}" size="10"></td>
                    <td align="center"><input type="text" name="rol" value="${fila.rol}" size="10"></td>
                    <td align="center"><input type="text" name="fecha_registro" value="${fila.fecha_registro}" size="15"></td>
                    <td align="center">
                        <div class="action-buttons">
                            
                                <input type="submit" value="Modificar" >
                                                                              
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${fila.id_usuario}">
                                <input type="button" value="Ver" onclick="window.location='UserForm?llave=${fila.id_usuario}'">
                            </form>
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${fila.id_usuario}">
                                <input type="button" value="XLS" onclick="window.location='UserFormXls?id_usuario=${fila.id_usuario}'">
                            </form>
                            <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${fila.id_usuario}">
                                <input type="button" value="PDF" onclick="window.location='UserFormPdf?id_usuario=${fila.id_usuario}'">
                            </form>
                           <form method="get" action="${pageContext.request.contextPath}/UsuarioView">
                                <input type="hidden" name="id_usuario" value="${fila.id_usuario}">
                                <input type="button" value="Html" onclick="window.location='UserFormHtml?id_usuario=${fila.id_usuario}'">
                            </form>
                        </div>
                    </td>
                </form>
            </tr>
        </c:forEach>
        </table>
        <br>
        <input type="button" value="Regresar" onclick="window.location='${pageContext.request.contextPath}/main.jsp'">
    </form>
    <br>
</body>
</html>
