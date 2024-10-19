package com.servlet_ordering_system.models.dtos;

import com.servlet_ordering_system.models.vos.Payment;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentDTO {

    private Long id;
    private Long orderId;
    private LocalDateTime moment;
    private Integer paymentMethodCode;

    public PaymentDTO() {
    }

    public PaymentDTO(Payment obj) {
        this.id = obj.getId();
        this.orderId = Objects.requireNonNull(obj.getOrder()).getId();
        this.moment = obj.getMoment();
        this.paymentMethodCode = Objects.requireNonNull(obj.getPaymentMethod()).getCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getMoment() {
        return moment;
    }

    public void setMoment(LocalDateTime moment) {
        this.moment = moment;
    }

    public Integer getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(Integer paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }
}
