<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String message = "Sessão expirada! clique em Continuar para efetuar o login.";
    if (request.getSession().getAttribute("message") != null) {
        message = (String) request.getSession().getAttribute("message");
    }

    String lastURIRequest = "login";
    if (request.getSession().getAttribute("lastURIRequest") != null) {
        lastURIRequest = (String) request.getSession().getAttribute("lastURIRequest");
    }
%>

    <html lang="pt-br">

        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="public/css/bootstrap.min.css">
            <title>Message</title>
        </head>

        <body class="d-flex justify-content-center align-items-center vh-100 bg-light">
            <div class="container text-center bg-white p-5 rounded shadow-sm">
                <h3 class="mb-4 text-primary"><%= message %></h3>
                <button onclick="location.href='<%= lastURIRequest %>'" class="btn btn-primary btn-lg">Continuar</button>
            </div>
        </body>

    </html>
