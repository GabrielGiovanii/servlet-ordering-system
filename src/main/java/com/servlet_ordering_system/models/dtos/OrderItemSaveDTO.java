package com.servlet_ordering_system.models.dtos;

public class OrderItemSaveDTO {

    private Long productId;
    private Integer quantity;

    public OrderItemSaveDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
