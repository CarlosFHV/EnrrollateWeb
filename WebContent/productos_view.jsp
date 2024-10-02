<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tienda en línea</title>
</head>
<body>

<table>
    <c:forEach var="producto" items="${productos}">
        <tr>
            <td>
                <div class="product">
                    <img src="${pageContext.request.contextPath}${producto.urlImagen}" height="auto" width="140" alt="${producto.nombre}">
                    <h2>${producto.nombre}</h2>
                    <p>${producto.descripcion}</p>
                    <p>Precio: ${producto.precio}</p>
                    <p>Stock: ${producto.stock}</p>

                    <!-- Formulario para agregar al carrito -->
<form action="${pageContext.request.contextPath}/agregarCarrito" method="post">
    <input type="hidden" name="idProducto" value="${producto.idProducto}">
    <label for="cantidad">Cantidad:</label>
    <input type="number" name="cantidad" value="1" min="1" required> <!-- Añadir este campo -->
    <button type="submit">Agregar al carrito</button>
</form>


                </div>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
