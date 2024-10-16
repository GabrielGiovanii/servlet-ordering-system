package com.servlet_ordering_system.models.dtos;

import java.util.ArrayList;
import java.util.List;

public class InsertOrderDTO {

    List<OrderItemSaveDTO> orderItems = new ArrayList<>();

    public InsertOrderDTO() {
    }

    public List<OrderItemSaveDTO> getOrderItems() {
        return orderItems;
    }
}
