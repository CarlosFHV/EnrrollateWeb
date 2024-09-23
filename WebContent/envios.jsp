<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestionar Env�os</title>
</head>
<body>
    <h1>Env�os</h1>
    <form action="EnvioServlet" method="post">
        <input type="text" name="id_pedido" placeholder="ID Pedido" required>
        <input type="date" name="fecha_envio" placeholder="Fecha de Env�o">
        <input type="date" name="fecha_entrega" placeholder="Fecha de Entrega">
        <select name="estado_envio">
            <option value="en tr�nsito">En Tr�nsito</option>
            <option value="entregado">Entregado</option>
            <option value="retrasado">Retrasado</option>
        </select>
        <input type="text" name="empresa_transporte" placeholder="Empresa de Transporte">
        <button type="submit">Registrar Env�o</button>
    </form>
    <h2>Lista de Env�os</h2>
    <!-- Aqu� ir�a un c�digo JSP para listar env�os desde la base de datos -->
</body>
</html>
