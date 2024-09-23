<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Envíos</title>
</head>
<body>
    <h1>Envíos</h1>
    <form action="EnvioServlet" method="post">
        <input type="text" name="id_pedido" placeholder="ID Pedido" required>
        <input type="date" name="fecha_envio" placeholder="Fecha de Envío">
        <input type="date" name="fecha_entrega" placeholder="Fecha de Entrega">
        <select name="estado_envio">
            <option value="en tránsito">En Tránsito</option>
            <option value="entregado">Entregado</option>
            <option value="retrasado">Retrasado</option>
        </select>
        <input type="text" name="empresa_transporte" placeholder="Empresa de Transporte">
        <button type="submit">Registrar Envío</button>
    </form>
    <h2>Lista de Envíos</h2>
    <!-- Aquí iría un código JSP para listar envíos desde la base de datos -->
</body>
</html>
