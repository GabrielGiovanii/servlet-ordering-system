package com.servlet_ordering_system.models.vos;

import java.util.Objects;

public class OrderItem {

    private Product product;
    private Order order;
    private Integer quantity;
    private Double price;

    public OrderItem() {
    }

    public OrderItem(Product product, Order order, Integer quantity, Double price) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public double subtotal() {
        return price * quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        OrderItem orderItem = (OrderItem) object;
        return Objects.equals(product, orderItem.product) && Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(product);
        result = 31 * result + Objects.hashCode(order);
        return result;
    }
}
