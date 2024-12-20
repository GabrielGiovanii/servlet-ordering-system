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
                <li class="nav-item dropdown">
                  <a class="nav-link dropdown-toggle text-white" role="button" data-bs-toggle="dropdown"
                    aria-expanded="false">
                    Usuário
                  </a>
                  <ul class="dropdown-menu">
                      <%
                          if (role.equals(Role.ADMIN)) {
                              out.print("<li><a class=\"dropdown-item\" onclick=\"getUserModal('insert', 1)\">Inserir</a></li>");
                              out.print("<li><a class=\"dropdown-item\" onclick=\"getUserModal('update', 1)\">Alterar</a></li>");
                          } else if (role.equals(Role.CLIENT)) {
                              out.print("<li><a class=\"dropdown-item\" onclick=\"getUserModal('update', 2)\">Alterar</a></li>");
                          }
                      %>
                    <li><a class="dropdown-item" onclick="getConfirmationModal('Tem certeza que deseja apagar o seu usuário?', 'deleteUserById(<%= user.getId() %>)')">Excluir</a></li>
                  </ul>
                </li>
                <li class="nav-item">
                  <a class="nav-link text-white" href="logout">Sair</a>
                </li>
              </ul>
              <a class="navbar-text ms-auto fs-5 text-white text-decoration-none nav-link">
                Olá <%= user.getName() %>
              </a>
              <input type="hidden" id="authenticatedUserId" value="<%= user.getId() %>">
            </div>
        </div>
    </nav>