package com.servlet_ordering_system.models.vos;

import com.servlet_ordering_system.models.vos.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

    private Long id;
    private LocalDateTime moment;
    private PaymentMethod paymentMethod;

    private Order order;

    public Payment() {
        this.moment = LocalDateTime.now();
    }

    public Payment(Long id, PaymentMethod paymentMethod, Order order) {
        this.id = id;
        this.moment = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.order = order;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Payment payment = (Payment) object;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
