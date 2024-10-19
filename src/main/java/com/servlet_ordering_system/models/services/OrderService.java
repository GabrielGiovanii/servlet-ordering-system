package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.controllers.exceptions.UnauthorizedException;
import com.servlet_ordering_system.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.OrderDAO;
import com.servlet_ordering_system.models.dtos.InsertOrderDTO;
import com.servlet_ordering_system.models.dtos.UpdateOrderDTO;
import com.servlet_ordering_system.models.services.contracts.CrudServiceWithUser;
import com.servlet_ordering_system.models.services.contracts.DtoConverter;
import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.OrderItem;
import com.servlet_ordering_system.models.vos.Product;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.OrderStatus;

import java.sql.Connection;
import java.util.List;

public class OrderService implements CrudServiceWithUser<Order>, DtoConverter<Order> {

    private final OrderDAO dao;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final Connection conn;

    public OrderService() {
        this.conn = DatabaseConnection.getConnection();
        this.dao = new OrderDAO();
        this.orderItemService = new OrderItemService(conn);
        this.productService = new ProductService();
    }

    public OrderService(Connection conn) {
        this.conn = conn;
        this.dao = new OrderDAO();
        this.orderItemService = new OrderItemService(conn);
        this.productService = new ProductService();
    }

    @Override
    public List<Order> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public List<Order> findAllWithUser(User user) {
        return dao.findAllWithUser(user, conn);
    }

    @Override
    public Order findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public Order findByIdWithUser(User user, Long id) {
        return dao.findByIdWithUser(user, conn, id);
    }

    @Override
    public Order findByString(String name) {
        return null;
    }

    @Override
    public Order findByStringWithUser(User user, String name) {
        return null;
    }

    @Override
    public Order insert(Order obj) {
        DatabaseConnection.setAutoCommit(conn, false);

        try {
            obj = dao.insert(conn, obj);

            obj.getOrderItems().forEach(orderItem ->
                    orderItem.setPrice(productService.findById(orderItem.getProduct().getId()).getPrice())
            );
            obj.getOrderItems().forEach(orderItemService::insert);

            DatabaseConnection.commit(conn);
        } catch (Exception e) {
            DatabaseConnection.rollback(conn);
            throw e;
        } finally {
            DatabaseConnection.setAutoCommit(conn, true);
        }

        return obj;
    }

    @Override
    public Order update(Order obj) {
        Order orderInDatabase = dao.findById(conn, obj.getId());
        orderInDatabase.setOrderStatus(obj.getOrderStatus());

        return dao.updateWithUser(conn, orderInDatabase);
    }

    @Override
    public Order updateWithUser(Order obj) {
        Order orderInDatabase = dao.findById(conn, obj.getId());

        if (orderInDatabase.getOrderStatus() != OrderStatus.WAITING_PAYMENT) {
            throw new UnauthorizedException("Pedido que seja diferente de WAITING_PAYMENT não pode ser cancelado!!!");
        }

        orderInDatabase.getOrderItems().forEach(orderItem -> obj.getOrderItems().add(orderItem));

        return dao.updateWithUser(conn, obj);
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public void deleteWithUser(User user, Long id) {
    }

    @Override
    public Order dtoToObject(Object dto) {
        Order obj = new Order();

        if (dto instanceof InsertOrderDTO insertOrderDTO) {
            insertOrderDTO.getOrderItems().forEach(ob -> {
                obj.setOrderStatus(OrderStatus.WAITING_PAYMENT);

                Product product = productService.findById(ob.getProductId());

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product);
                orderItem.setOrder(obj);
                orderItem.setQuantity(ob.getQuantity());

                product.getOrderItems().add(orderItem);
                obj.getOrderItems().add(orderItem);
            });
        } else if (dto instanceof UpdateOrderDTO updateOrderDTO) {
            obj.setId(updateOrderDTO.getId());
            obj.setOrderStatus(OrderStatus.CANCELED);
        }

        return obj;
    }
}
