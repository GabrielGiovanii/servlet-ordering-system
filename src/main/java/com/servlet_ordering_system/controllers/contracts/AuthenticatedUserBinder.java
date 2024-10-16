package com.servlet_ordering_system.controllers.contracts;

import com.servlet_ordering_system.models.vos.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticatedUserBinder<T> {

    User getAuthenticatedUser(HttpServletRequest req);

    void associateUserWithOrder(HttpServletRequest req, T obj);
}
