package com.servlet_ordering_system.security;

import com.servlet_ordering_system.security.enums.HttpVerb;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;

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

    private static final String AUTH;
    private static final String USERS;
    private static final String CATEGORIES;
    private static final String PRODUCTS;
    private static final String ORDERS;
    private static final String PAYMENTS;

    static {
        GUEST = "GUEST";
        ADMIN = Role.ADMIN.name();
        CLIENT = Role.CLIENT.name();

        AUTH = "auth";
        USERS = "users";
        CATEGORIES = "categories";
        PRODUCTS = "products";
        ORDERS = "orders";
        PAYMENTS = "payments";
    }

    public static Map<String, List<HttpVerb>> mapPermissionsApiServlet(String key) {
        Map<String, Map<String, List<HttpVerb>>> permissions = Map.of(
                GUEST, Map.of(
                        AUTH, List.of(POST)
                ),
                ADMIN, Map.of(
                        AUTH, List.of(POST),
                        USERS, List.of(GET, POST, PUT, DELETE),
                        CATEGORIES, List.of(GET, POST, PUT, DELETE),
                        PRODUCTS, List.of(GET, POST, PUT, DELETE),
                        ORDERS, List.of(GET),
                        PAYMENTS, List.of(GET)
                ),
                CLIENT, Map.of(
                        AUTH, List.of(POST),
                        USERS, List.of(GET, POST, PUT, DELETE),
                        CATEGORIES, List.of(GET),
                        PRODUCTS, List.of(GET),
                        ORDERS, List.of(GET, POST, PUT),
                        PAYMENTS, List.of(GET, POST)
                )
        );

        return permissions.get(key);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String servletPath =  httpRequest.getServletPath();
        String [] pathParts = servletPath.substring(1).split("/");
        String pathServlet = pathParts[0];

        if (pathServlet.equals("public") ||
                pathServlet.endsWith("login.jsp") ||
                pathServlet.endsWith("message.jsp")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        User authenticatedUser = getAuthenticatedUserFromSession(httpRequest);
        
        Map<String, List<HttpVerb>> permissions = getPermissionsForUser(authenticatedUser);

        handleRequestAuthorization(filterChain, httpRequest, permissions, pathServlet, httpResponse);
    }

    private static void handleRequestAuthorization(FilterChain filterChain, HttpServletRequest httpRequest, Map<String, List<HttpVerb>> permissions, String path, HttpServletResponse httpResponse) throws IOException, ServletException {
        String origin = httpRequest.getHeader("origin-of-request");
        if (Objects.nonNull(origin) && origin.equals("api-servlet")) {
            String requestMethod = httpRequest.getMethod();

            List<HttpVerb> httpVerbsAllowed = permissions.get(path.toLowerCase());

            boolean permitted = false;
            if (Objects.nonNull(httpVerbsAllowed)) {
                for (HttpVerb verb : httpVerbsAllowed) {
                    if (verb.name().equals(requestMethod)) {
                        filterChain.doFilter(httpRequest, httpResponse);

                        permitted = true;
                        break;
                    }
                }
            }

            if (!permitted) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/message.jsp");
        }
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
