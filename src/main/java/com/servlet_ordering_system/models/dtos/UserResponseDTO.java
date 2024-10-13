package com.servlet_ordering_system.models.dtos;

import com.servlet_ordering_system.models.vos.User;

import java.util.Objects;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;;
    private Integer orderStatusId;

    public UserResponseDTO(User obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.email = obj.getEmail();
        this.phone = obj.getPhone();
        this.orderStatusId = Objects.requireNonNull(obj.getRole()).getCode();
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

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }
}
