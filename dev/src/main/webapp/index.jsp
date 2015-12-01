<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>.......:::::Contexto de Reportes:::::.......</title>
</head>
<body>

<h1>Bienvenido al contexto de Reportes</h1>

<% 

String nombreContexto = request.getContextPath()+"/";
String version = application.getInitParameter("VERSIONAXIOMA");
String tipoAmbiente = application.getInitParameter("TIPOAMBIENTE");

if(tipoAmbiente.equals("desarrollo") || tipoAmbiente.equals("pruebas")) { %>
<p class="subtitulo2">
	Ver: <%=version%>
</p>
<% } else  {%>
	<p>&nbsp;</p>
<% } %>

</body>
</html>