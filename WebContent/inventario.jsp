<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Inventario</title>
</head>
<body>
    <h1>Inventario</h1>
    <form action="InventarioServlet" method="post">
        <input type="text" name="id_producto" placeholder="ID Producto" required>
        <input type="number" name="cantidad_disponible" placeholder="Cantidad Disponible" required>
        <input type="text" name="ubicacion_almacen" placeholder="Ubicaci�n en el Almac�n">
        <button type="submit">Actualizar Inventario</button>
    </form>
    <h2>Lista de Inventario</h2>
    <!-- Aqu� ir�a un c�digo JSP para listar el inventario desde la base de datos -->
</body>
</html>
