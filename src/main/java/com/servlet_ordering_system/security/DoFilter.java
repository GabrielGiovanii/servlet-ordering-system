package com.servlet_ordering_system.security;

import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;
import com.servlet_ordering_system.security.enums.HttpVerb;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    }

    public static Map<String, List<HttpVerb>> mapPermissionsApiServlet(String key) {
        Map<String, Map<String, List<HttpVerb>>> permissions = Map.of(
                GUEST, Map.of(
                        AUTH_API, List.of(POST),
                        USERS_API, List.of(POST),
                        LOGIN_VIEW, List.of(GET),
                        MESSAGE_VIEW, List.of(GET),
                        PUBLIC, List.of(GET)
                ),
                ADMIN, Map.of(
                        AUTH_API, List.of(POST),
                        USERS_API, List.of(GET, POST, PUT, DELETE),
                        CATEGORIES_API, List.of(GET, POST, PUT, DELETE),
                        PRODUCTS_API, List.of(GET, POST, PUT, DELETE),
                        ORDERS_API, List.of(GET),
                        PAYMENTS_API, List.of(GET),
                        LOGIN_VIEW, List.of(GET),
                        MESSAGE_VIEW, List.of(GET),
                        MANAGER_VIEW, List.of(GET),
                        PUBLIC, List.of(GET)
                ),
                CLIENT, Map.of(
                        AUTH_API, List.of(POST),
                        USERS_API, List.of(GET, POST, PUT, DELETE),
                        CATEGORIES_API, List.of(GET),
                        PRODUCTS_API, List.of(GET),
                        ORDERS_API, List.of(GET, POST, PUT),
                        PAYMENTS_API, List.of(GET, POST),
                        LOGIN_VIEW, List.of(GET),
                        MESSAGE_VIEW, List.of(GET),
                        HOME_VIEW, List.of(GET),
                        PUBLIC, List.of(GET)
                )
        );

        return permissions.get(key);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

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
                message = "Sessão expirada! clique em Continuar para efetuar o login.";
                lastURIRequest = "login";
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
