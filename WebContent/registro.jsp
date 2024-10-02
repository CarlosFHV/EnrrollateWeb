<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="javax.naming.*" %>
<%@ page import="javax.sql.*" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=windows-1252">
    <title>Registro</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <table border="0" cellpadding="0" cellspacing="0" width="800">
        <tbody>
            <tr>
                <td colspan="3" align="center" height="20"> <!-- [[HEADER]] --> </td>
            </tr>
            <tr>
                <td class="menu" align="center" valign="top" width="150">
                    <!-- [[MENU]] --> 
                </td>
                <td width="25"> <br> </td>
                <td align="center" valign="top" width="625">
                    <div class="section">
                        <h2>Registro de Usuario</h2>
                        <form method="post" action="RegistrarServlet"> 
                            <table>
                                <tbody>
                                    <tr class="form">
                                        <td align="right">
                                            <div class="label"> Nombre: </div>
                                        </td>
                                        <td> <input name="nombre" required> </td> 
                                    </tr>
                                    <tr class="form">
                                        <td align="right">
                                            <div class="label"> Email: </div>
                                        </td>
                                        <td> <input name="email" type="email" required> </td>
                                    </tr>
                                    <tr class="form">
                                        <td align="right">
                                            <div class="label"> Contraseña: </div>
                                        </td>
                                        <td> <input name="password" type="password" required> </td>
                                    </tr>
                                    <tr class="form">
                                        <td align="right">
                                            <div class="label"> Dirección: </div>
                                        </td>
                                        <td> <input name="direccion" required> </td>
                                    </tr>
                                    <tr class="form">
                                        <td align="right">
                                            <div class="label"> Teléfono: </div>
                                        </td>
                                        <td> <input name="telefono" required> </td>
                                    </tr>
           
                                    <tr>
                                        <td><br></td>
                                        <td>
                                            <input type="submit" value="Registrar">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="3" align="center">
                    <!-- [[FOOTER]] --> 
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>
