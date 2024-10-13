package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.controllers.exceptions.UnauthorizedException;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.security.PasswordUtil;

import java.util.Objects;

public class AuthService {

    private final UserService userService;

    public AuthService() {
        this.userService = new UserService();
    }

    public User authenticate(String email, String password) {
        User user = userService.findByName(email);

        if (Objects.nonNull(user)) {
            if (!PasswordUtil.passwordIsValid(password, user.getPassword())) {
                throw new UnauthorizedException("Senha inválida!!!");
            }
        } else {
            throw new UnauthorizedException("Usuário não localizado!!!");
        }

        return user;
    }
}
