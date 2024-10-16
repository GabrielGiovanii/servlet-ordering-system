package com.servlet_ordering_system.models.dtos;

import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderResponseDTO {

    private Long id;
    private LocalDateTime moment;
    private Integer orderStatusCode;
    private OrderStatus orderStatus;
    private Long clientId;
    private Double total;

    List<OrderItemResponseDTO> orderItems = new ArrayList<>();

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order obj) {
        this.id = obj.getId();
        this.moment = obj.getMoment();
        this.orderStatusCode = obj.getOrderStatus().getCode();
        this.orderStatus = obj.getOrderStatus();
        this.clientId = Objects.requireNonNull(obj.getClient()).getId();
        this.total = obj.total();

        obj.getOrderItems().forEach(orderItem -> orderItems.add(new OrderItemResponseDTO(orderItem)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public void setMoment(LocalDateTime moment) {
        this.moment = moment;
    }

    public Integer getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(Integer orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<OrderItemResponseDTO> getOrderItems() {
        return orderItems;
    }
}
