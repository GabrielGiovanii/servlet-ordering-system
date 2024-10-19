package com.servlet_ordering_system.models.vos;

import com.servlet_ordering_system.models.vos.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order {

    private Long id;
    private LocalDateTime moment;
    private OrderStatus orderStatus;

    private User client;
    private Payment payment;

    private final Set<OrderItem> orderItems = new HashSet<>();

    public Order() {
        this.moment = LocalDateTime.now();
    }

    public Order(Long id, OrderStatus orderStatus, User client) {
        this.id = id;
        this.moment = LocalDateTime.now();
        this.orderStatus = orderStatus;
        this.client = client;
    }

    public Order(Long id, LocalDateTime moment, OrderStatus orderStatus, User client) {
        this.id = id;
        this.moment = moment;
        this.orderStatus = orderStatus;
        this.client = client;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double total() {
        return orderItems.stream()
                .mapToDouble(OrderItem::subTotal)
                .sum();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Order order = (Order) object;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
