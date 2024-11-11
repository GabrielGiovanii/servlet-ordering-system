<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.servlet_ordering_system.models.vos.User, com.servlet_ordering_system.models.vos.enums.Role" %>

<%
    User user = ((User) request.getSession().getAttribute("authenticatedUser"));

    Role role = user.getRole();

    String profile = null;

    if (role.equals(Role.ADMIN)) {
        profile = "Perfil do Gerente";
    } else if (role.equals(Role.CLIENT)) {
        profile = "Perfil do Cliente";
    }

    String mainPage = ((String) request.getSession().getAttribute("mainPage"));
%>

    <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand text-white"><%= profile %></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
              <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
              <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                  <a class="nav-link text-white" aria-current="page"><%= mainPage %></a>
                </li>
                <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle text-white" role="button" data-bs-toggle="dropdown"
                    aria-expanded="false">
                    Usuário
                  </a>
                  <ul class="dropdown-menu">
                    <li><a class="dropdown-item">Alterar</a></li>
                    <li><a class="dropdown-item">Excluir</a></li>
                  </ul>
                </li>
                <li class="nav-item">
                  <a class="nav-link text-white" href="logout">Sair</a>
                </li>
              </ul>
              <a class="navbar-text ms-auto fs-5 text-white text-decoration-none nav-link">
                Olá <%= user.getName() %>
              </a>
            </div>
        </div>
    </nav>