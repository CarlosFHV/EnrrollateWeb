<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
	if (session == null || session.getAttribute("email") == null || session.getAttribute("idUsuario") == null) {
    	response.sendRedirect("login.jsp"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }
%>
<%@ page import="mx.uam.azc.arachnocoders.enrrollate.data.CarritoDTO" %>

<html>
<head>
    <title>Carrito de Compras</title>
    <script>
        // Función para enviar el formulario automáticamente cuando cambie la cantidad
        function actualizarCarrito(form) {
            form.submit();
        }
    </script>
</head>
<body>
    <h1>Carrito de Compras</h1>
    <form action="ActualizarCarritoServlet" method="post">
        <table border="1">
            <tr>
                <th>ID Producto</th>
                <th>Nombre</th>
                <th>Cantidad</th>
            </tr>
            <%
                List<CarritoDTO> carritoList = (List<CarritoDTO>) request.getAttribute("carritoList");
                if (carritoList != null && !carritoList.isEmpty()) {
                    for (CarritoDTO item : carritoList) {
            %>
            <tr>
                <td><%= item.getIdProducto() %></td>
                <td><%= item.getNombre() %></td>
                <td>
                    <!-- Input number que envía el formulario automáticamente al cambiar -->
                    <input type="number" name="cantidad_<%= item.getIdProducto() %>" value="<%= item.getCantidad() %>" min="0" onchange="actualizarCarrito(this.form)">
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="3">El carrito está vacío.</td>
            </tr>
            <%
                }
            %>
        </table>
    </form>
    <%
        if (carritoList != null && !carritoList.isEmpty()) {
    %>
    <!-- Formulario para finalizar pedido -->
    <h2>Finalizar Pedido</h2>
    <form action="realizarPedido" method="post">
        <label for="direccionEnvio">Dirección de Envío:</label><br>
        <input type="text" id="direccionEnvio" name="direccionEnvio" required><br><br>
        <input type="submit" value="Realizar Pedido">
    </form>
    
    <%
                }
            %>
</body>
</html>
