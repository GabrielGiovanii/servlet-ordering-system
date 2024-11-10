package com.servlet_ordering_system.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class MessageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/message.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }
}
