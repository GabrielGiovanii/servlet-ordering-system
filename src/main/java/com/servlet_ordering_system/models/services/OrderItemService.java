package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.models.daos.OrderItemDAO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.vos.OrderItem;

import java.sql.Connection;
import java.util.List;

public class OrderItemService implements CrudService<OrderItem> {

    private final OrderItemDAO dao;;
    private final Connection conn;

    public OrderItemService(Connection conn) {
        this.dao = new OrderItemDAO();
        this.conn = conn;
    }

    @Override
    public List<OrderItem> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public OrderItem findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public OrderItem findByString(String string) {
        return dao.findByString(conn, string);
    }

    @Override
    public OrderItem insert(OrderItem obj) {
        return dao.insert(conn, obj);
    }

    @Override
    public OrderItem update(OrderItem obj) {
        return dao.update(conn, obj);
    }

    @Override
    public void delete(Long id) {
        dao.delete(conn, id);
    }
}
