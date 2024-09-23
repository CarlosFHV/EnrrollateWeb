<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Proveedores</title>
</head>
<body>
    <h1>Proveedores</h1>
    <form action="ProveedorServlet" method="post">
        <input type="text" name="nombre_proveedor" placeholder="Nombre del Proveedor" required>
        <input type="text" name="contacto" placeholder="Contacto">
        <input type="text" name="telefono" placeholder="Teléfono">
        <input type="email" name="email" placeholder="Email">
        <button type="submit">Agregar Proveedor</button>
    </form>
    <h2>Lista de Proveedores</h2>
    <!-- Aquí iría un código JSP para listar proveedores desde la base de datos -->
</body>
</html>
