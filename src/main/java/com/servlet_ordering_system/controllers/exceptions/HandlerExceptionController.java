package com.servlet_ordering_system.controllers.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

public abstract class HandlerExceptionController {

    public static void handleExceptionResponse(Exception e, HttpServletResponse resp) {
        if (e instanceof SecurityException || e instanceof IllegalArgumentException
                || e instanceof UnrecognizedPropertyException) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (e instanceof UnauthorizedException) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
