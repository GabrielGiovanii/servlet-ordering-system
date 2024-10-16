package com.servlet_ordering_system.models.dtos;

import com.servlet_ordering_system.models.vos.OrderItem;

import java.util.Objects;

public class OrderItemResponseDTO {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double subTotal;

    public OrderItemResponseDTO() {
    }

    public OrderItemResponseDTO(OrderItem obj) {
        this.productId = Objects.requireNonNull(obj.getProduct()).getId();
        this.productName = Objects.requireNonNull(obj.getProduct()).getName();
        this.quantity = obj.getQuantity();
        this.price = obj.getPrice();
        this.subTotal = obj.subTotal();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }
}
