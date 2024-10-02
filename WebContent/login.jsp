<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    if (session != null && session.getAttribute("email") != null ) { // Cambia a email
    	response.sendRedirect("VerProductos"); // Redirige si no hay sesión
        
        return; // Termina la ejecución
    }

		

%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=windows-1252">
    <meta name="Author" content="Hugo Pablo Leyva">
    <title>LOGIN</title>
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
                    <div class="section"> </div>
                    
                    <form method="post" action="Login"> 
                        <div class="step"> Proporciona tu Email y tu contraseña para tener acceso a la aplicación </div>
                        <table>
                            <tbody>
                                <tr class="form">
                                    <td align="right">
                                        <div class="label"> Email: </div>
                                    </td>
                                    <td> <input name="email" size="10"> </td> 
                                </tr>
                                <tr class="form">
                                    <td align="right">
                                        <div class="label"> Contraseña: </div>
                                    </td>
                                    <td> <input name="password" size="10" type="password"> </td>
                                </tr>
                                <tr>
                                    <td><br></td>
                                    <td>
                                        <input type="submit" value="Login">
                                        <input type="button" value="Registrar" onclick="window.location.href='registro.jsp'"> 
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </td>
            </tr>
            <tr>
                <td colspan="3" align="center">
                    <!-- [[FOOTER]] --> 
                </td>
            </tr>
        </tbody>
    </table>
    
    <% 
        // Mensaje de error si está presente
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
        <p style='color:red;'><%= errorMessage %></p>
    <% } %>
</body>
</html>
