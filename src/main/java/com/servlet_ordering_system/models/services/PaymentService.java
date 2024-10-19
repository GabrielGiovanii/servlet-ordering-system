package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.config.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.PaymentDAO;
import com.servlet_ordering_system.models.dtos.PaymentDTO;
import com.servlet_ordering_system.models.services.contracts.CrudServiceWithUser;
import com.servlet_ordering_system.models.services.contracts.DtoConverter;
import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.Payment;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.OrderStatus;
import com.servlet_ordering_system.models.vos.enums.PaymentMethod;

import java.sql.Connection;
import java.util.List;

public class PaymentService implements CrudServiceWithUser<Payment>, DtoConverter<Payment> {

    private final PaymentDAO dao;
    private final OrderService orderService;
    private final Connection conn;

    public PaymentService() {
        this.conn = DatabaseConnection.getConnection();
        this.dao = new PaymentDAO();
        this.orderService = new OrderService(conn);
    }

    @Override
    public List<Payment> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public List<Payment> findAllWithUser(User user) {
        return dao.findAllWithUser(user, conn);
    }

    @Override
    public Payment findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public Payment findByIdWithUser(User user, Long id) {
        return dao.findByIdWithUser(user, conn, id);
    }

    @Override
    public Payment findByString(String name) {
        return null;
    }

    @Override
    public Payment findByStringWithUser(User user, String name) {
        return null;
    }

    @Override
    public Payment insert(Payment obj) {
        DatabaseConnection.setAutoCommit(conn, false);

        try {
            obj = dao.insert(conn, obj);

            orderService.update(obj.getOrder());

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
    public Payment updateWithUser(Payment obj) {
        return null;
    }

    @Override
    public Payment update(Payment obj) {
        return null;
    }

    @Override
    public void deleteWithUser(User user, Long id) {
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public Payment dtoToObject(Object dto) {
        Payment obj = new Payment();

        if (dto instanceof PaymentDTO paymentDTO) {
            obj.setId(paymentDTO.getId());

            Order order = new Order();
            order.setId(paymentDTO.getOrderId());
            order.setOrderStatus(OrderStatus.PAID);

            obj.setOrder(order);
            order.setPayment(obj);

            obj.setPaymentMethod(PaymentMethod.valueOf(paymentDTO.getPaymentMethodCode()));
        }

        return obj;
    }
}
