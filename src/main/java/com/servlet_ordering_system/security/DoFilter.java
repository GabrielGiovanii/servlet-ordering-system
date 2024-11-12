package com.servlet_ordering_system.security;

import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;
import com.servlet_ordering_system.security.enums.HttpVerb;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.servlet_ordering_system.security.enums.HttpVerb.*;

public class DoFilter implements Filter {

    private static final String GUEST;
    private static final String ADMIN;
    private static final String CLIENT;

    private static final String AUTH_API;
    private static final String USERS_API;
    private static final String CATEGORIES_API;
    private static final String PRODUCTS_API;
    private static final String ORDERS_API;
    private static final String PAYMENTS_API;

    private static final String LOGIN_VIEW;
    private static final String MESSAGE_VIEW;
    private static final String HOME_VIEW;
    private static final String MANAGER_VIEW;

    private static final String PUBLIC;
    private static final String LOGOUT;

    static {
        GUEST = "GUEST";
        ADMIN = Role.ADMIN.name();
        CLIENT = Role.CLIENT.name();

        AUTH_API = "/api/auth";
        USERS_API = "/api/users";
        CATEGORIES_API = "/api/categories";
        PRODUCTS_API = "/api/products";
        ORDERS_API = "/api/orders";
        PAYMENTS_API = "/api/payments";

        LOGIN_VIEW = "/login";
        MESSAGE_VIEW = "/message";
        HOME_VIEW = "/home";
        MANAGER_VIEW = "/manager";

        PUBLIC = "/public";
        LOGOUT = "/logout";
    }

    public static Map<String, List<HttpVerb>> mapPermissionsApiServlet(String key) {
        Map<String, List<HttpVerb>> guest = new HashMap<>();
        guest.put(AUTH_API, List.of(POST));
        guest.put(USERS_API, List.of(POST));
        guest.put(LOGIN_VIEW, List.of(GET));
        guest.put(MESSAGE_VIEW, List.of(GET));
        guest.put(PUBLIC, List.of(GET));

        Map<String, List<HttpVerb>> admin = new HashMap<>();
        admin.put(AUTH_API, List.of(POST));
        admin.put(USERS_API, List.of(GET, POST, PUT, DELETE));
        admin.put(CATEGORIES_API, List.of(GET, POST, PUT, DELETE));
        admin.put(PRODUCTS_API, List.of(GET, POST, PUT, DELETE));
        admin.put(ORDERS_API, List.of(GET));
        admin.put(PAYMENTS_API, List.of(GET));
        admin.put(LOGIN_VIEW, List.of(GET));
        admin.put(MESSAGE_VIEW, List.of(GET));
        admin.put(MANAGER_VIEW, List.of(GET));
        admin.put(PUBLIC, List.of(GET));
        admin.put(LOGOUT, List.of(GET));

        Map<String, List<HttpVerb>> client = new HashMap<>();
        client.put(AUTH_API, List.of(POST));
        client.put(USERS_API, List.of(GET, POST, PUT, DELETE));
        client.put(CATEGORIES_API, List.of(GET));
        client.put(PRODUCTS_API, List.of(GET));
        client.put(ORDERS_API, List.of(GET, POST, PUT));
        client.put(PAYMENTS_API, List.of(GET, POST));
        client.put(LOGIN_VIEW, List.of(GET));
        client.put(MESSAGE_VIEW, List.of(GET));
        client.put(HOME_VIEW, List.of(GET));
        client.put(PUBLIC, List.of(GET));
        client.put(LOGOUT, List.of(GET));

        Map<String, Map<String, List<HttpVerb>>> permissions = Map.ofEntries(
                Map.entry(GUEST, guest),
                Map.entry(ADMIN, admin),
                Map.entry(CLIENT, client)
        );


        return permissions.get(key);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        httpResponse.setContentType("application/json; charset=UTF-8");

        String servletPath =  httpRequest.getServletPath();

        User authenticatedUser = getAuthenticatedUserFromSession(httpRequest);
        Map<String, List<HttpVerb>> permissions = getPermissionsForUser(authenticatedUser);

        handleRequestAuthorization(filterChain, httpRequest, authenticatedUser, permissions, servletPath, httpResponse);
    }

    private static void handleRequestAuthorization(FilterChain filterChain, HttpServletRequest httpRequest,
                                                   User authenticatedUser, Map<String, List<HttpVerb>> permissions,
                                                   String servletPath, HttpServletResponse httpResponse) throws IOException, ServletException {
        String requestMethod = httpRequest.getMethod();

        List<HttpVerb> httpVerbsAllowed = findAllowedHttpVerbs(permissions, servletPath);

        boolean permitted = isRequestMethodPermitted(httpVerbsAllowed, requestMethod);

        if (permitted) {
            filterChain.doFilter(httpRequest, httpResponse);
        } else {
            String message = null;
            String lastURIRequest = null;

            boolean isServletMapped = isServletMapped(httpRequest, servletPath);

            if (Objects.isNull(authenticatedUser)) {
                if (servletPath.startsWith("/api")) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else {
                    message = "Sessão expirada! clique em Continuar para efetuar o login.";
                    lastURIRequest = "login";
                }
            } else if(!isServletMapped) {
                message = "Recurso não encontrado! clique em Continuar para retornar ao sistema.";
                lastURIRequest = "login";
            } else {
                message = "Recurso não autorizado! clique em Continuar para retornar ao sistema.";

                Role userRole = authenticatedUser.getRole();

                lastURIRequest = httpRequest.getContextPath();
                if (userRole.equals(Role.ADMIN)) {
                    lastURIRequest += MANAGER_VIEW;
                } else if (userRole.equals(Role.CLIENT)) {
                    lastURIRequest += HOME_VIEW;
                }
            }

            httpRequest.getSession().setAttribute("message", message);
            httpRequest.getSession().setAttribute("lastURIRequest", lastURIRequest);

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/message");

        }
    }

    private static List<HttpVerb> findAllowedHttpVerbs(Map<String, List<HttpVerb>> permissions, String servletPath) {
        List<HttpVerb> httpVerbsAllowed = permissions.get(servletPath.toLowerCase());

        if (Objects.isNull(httpVerbsAllowed)) {
            if (servletPath.equals("/")) {
                return null;
            }

            String [] partsPath = servletPath.split("/");
            httpVerbsAllowed = permissions.get("/" + partsPath[1].toLowerCase());
        }
        return httpVerbsAllowed;
    }

    private static boolean isRequestMethodPermitted(List<HttpVerb> httpVerbsAllowed, String requestMethod) {
        boolean permitted = false;
        if (Objects.nonNull(httpVerbsAllowed)) {
            for (HttpVerb verb : httpVerbsAllowed) {
                if (verb.name().equals(requestMethod)) {
                    permitted = true;
                    break;
                }
            }
        }
        return permitted;
    }

    private static boolean isServletMapped(HttpServletRequest httpRequest, String servletPath) {
        ServletContext context = httpRequest.getServletContext();
        boolean isServletMapped = false;

        for (ServletRegistration servlet : context.getServletRegistrations().values()) {
            if (servlet.getMappings().contains(servletPath)) {
                isServletMapped = true;
                break;
            }
        }
        return isServletMapped;
    }

    private static Map<String, List<HttpVerb>> getPermissionsForUser(User authenticatedUser) {
        if (Objects.nonNull(authenticatedUser)) {
            return mapPermissionsApiServlet(authenticatedUser.getRole().name());
        } else {
            return mapPermissionsApiServlet(GUEST);
        }
    }

    public static User getAuthenticatedUserFromSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();
        if (Objects.nonNull(session.getAttribute("authenticatedUser"))) {
            return (User) session.getAttribute("authenticatedUser");
        } else {
            return null;
        }
    }
}
