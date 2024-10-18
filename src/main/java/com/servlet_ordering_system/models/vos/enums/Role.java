package com.servlet_ordering_system.models.vos.enums;

public enum Role {

    ADMIN(1),
    CLIENT(2);

    private final int code;

    private Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role valueOf(int code) {
        for(Role role : Role.values()) {
            if(role.getCode() == code)
                return role;
        }

        return null;
    }
}
