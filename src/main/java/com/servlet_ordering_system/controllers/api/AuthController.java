package com.servlet_ordering_system.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.models.dtos.AuthDTO;
import com.servlet_ordering_system.models.services.AuthService;
import com.servlet_ordering_system.models.vos.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class AuthController extends HttpServlet {

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public AuthController() {
        this.authService = new AuthService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            AuthDTO authDTO = objectMapper.readValue(req.getInputStream(), AuthDTO.class);

            User authenticatedUser = authService.authenticate(authDTO.getEmail(), authDTO.getPassword());

            if (Objects.nonNull(authenticatedUser)) {
                HttpSession currentSession = req.getSession(false);

                if (Objects.nonNull(currentSession)) {
                    currentSession.invalidate();
                }

                HttpSession newSession = req.getSession(true);
                newSession.setAttribute("authenticatedUser", authenticatedUser);

                resp.setStatus(HttpServletResponse.SC_CREATED);
            }
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }
}
