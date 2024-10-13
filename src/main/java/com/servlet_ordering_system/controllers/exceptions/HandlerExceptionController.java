package com.servlet_ordering_system.controllers.exceptions;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

public abstract class HandlerExceptionController {

    public static void handleExceptionResponse(Exception e, HttpServletResponse resp) {
        if (e instanceof NumberFormatException || e instanceof SecurityException) {
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
