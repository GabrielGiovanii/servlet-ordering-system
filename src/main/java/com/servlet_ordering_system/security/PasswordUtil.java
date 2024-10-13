package com.servlet_ordering_system.security;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class PasswordUtil {

    public static String encryptPassword(String password) {
        if (Objects.isNull(password) || password.isBlank()) {
            throw new SecurityException("Informar a senha é obrigatório!!!");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean passwordIsValid(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
