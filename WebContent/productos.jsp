<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Productos</title>
</head>
<body>
    <h1>Productos</h1>
    <form action="ProductoServlet" method="post">
        <input type="text" name="nombre_producto" placeholder="Nombre del Producto" required>
        <textarea name="descripcion" placeholder="Descripción"></textarea>
        <input type="number" step="0.01" name="precio" placeholder="Precio" required>
        <input type="number" name="stock" placeholder="Cantidad en Stock" required>
        <button type="submit">Agregar Producto</button>
    </form>
    <h2>Lista de Productos</h2>
    <!-- Aquí iría un código JSP para listar productos desde la base de datos -->
</body>
</html>
