<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String message = "Sessão expirada! clique em Continuar para efetuar o login.";
    if (request.getSession().getAttribute("message") != null) {
        message = (String) request.getSession().getAttribute("message");
    }

    String lastURIRequest = "login.jsp";
    if (request.getSession().getAttribute("lastURIRequest") != null) {
        lastURIRequest = (String) request.getSession().getAttribute("lastURIRequest");
    }
%>

<html>
    <body>
        <p><%= message %><p>
        <button onclick="location.href='<%= lastURIRequest %>'">Continuar</button>
    </body>
</html>
