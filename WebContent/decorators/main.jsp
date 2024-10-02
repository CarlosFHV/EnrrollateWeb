<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator"
    prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<html>
<head>
<title><decorator:title default="Titulo" /></title>
<link rel="stylesheet"
    href="${ pageContext.request.contextPath }/css/style.css">
<decorator:head />
</head>

<body>

<div class="table-container">

    <table align="center" border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr style="background-color: lightblue; !important">
            <td align="center" colspan="3" height="20">
            <%@include file="header.jspf"%>
            </td>
        </tr>
        <tr>
            <td align="center" valign="top" width="150" class="menu">
            
            <%@ page import="javax.servlet.http.HttpSession" %>
		<%
		    if (session != null && session.getAttribute("email") != null) { %>
		                        <%@include file="menu.jspf"%>
		        
	<%	    }
		%>
            
            </td>
            <td valign="top" align="center" width="625">
                
                <hr> <decorator:body />
                <br>
                <br>
            </td>
            <td align="center" valign="top" width="150" class="menu">
            </td>
        </tr>
        <tr style="background-color: lightblue;">
            <td  colspan="3" >
            <%@include file="footer.jspf"%>
                    </td>
        </tr>
    </table>
</div>
</body>
</html>