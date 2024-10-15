package com.servlet_ordering_system.models.dtos;

import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;

import java.util.Objects;

public class UserSaveDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Integer roleId;

    public UserSaveDTO() {
    }

    public User dtoToObject(UserSaveDTO dto) {
        User obj = new User();
        obj.setId(dto.getId());
        obj.setName(dto.getName());
        obj.setEmail(dto.getEmail());
        obj.setPhone(dto.getPhone());
        obj.setPassword(dto.getPassword());
        obj.setRole(Objects.requireNonNull(Role.valueOf(dto.getRoleId())));

        return obj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
