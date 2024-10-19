package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAOWithUser;
import com.servlet_ordering_system.models.daos.contracts.RelationalMapping;
import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.Payment;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.PaymentMethod;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PaymentDAO implements CrudDAOWithUser<Payment>, RelationalMapping<Payment> {

    @Override
    public List<Payment> findAll(Connection conn) {
        List<Payment> payments = new ArrayList<>();

        String commandSql = """
                SELECT id, order_id, payment_method, moment FROM tb_payment
                ORDER BY moment;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(objectRelationalMapping(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return payments;
    }

    @Override
    public List<Payment> findAllWithUser(User user, Connection conn) {
        List<Payment> payments = new ArrayList<>();

        String commandSql = """
                SELECT p.id, p.order_id, p.payment_method, p.moment FROM tb_payment p\s
                JOIN tb_order o ON p.order_id = o.id\s
                JOIN tb_user u ON o.client_id = u.id\s
                WHERE u.id = ?\s
                ORDER BY moment;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
             ps.setLong(1, user.getId());

             ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(objectRelationalMapping(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return payments;
    }

    @Override
    public Payment findById(Connection conn, Long id) {
        String commandSql = """
                SELECT p.id, p.order_id, p.payment_method, p.moment FROM tb_payment p;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Payment findByIdWithUser(User user, Connection conn, Long id) {
        String commandSql = """
                SELECT p.id, p.order_id, p.payment_method, p.moment FROM tb_payment p\s
                JOIN tb_order o ON p.order_id = o.id\s
                JOIN tb_user u ON o.client_id = u.id\s
                WHERE u.id = ? AND p.id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, user.getId());
            ps.setLong(2, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Payment findByString(Connection conn, String string) {
        return null;
    }

    @Override
    public Payment findByStringWithUser(User user, Connection conn, String string) {
        return null;
    }

    @Override
    public Payment insert(Connection conn, Payment obj) {
        String commandSql = """
                INSERT INTO tb_payment (order_id, payment_method, moment)
                VALUES (?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, obj.getOrder().getId());
            ps.setInt(2, obj.getPaymentMethod().getCode());
            ps.setTimestamp(3, Timestamp.valueOf(obj.getMoment()));

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        obj.setId(id);
                    }
                }
            } else {
                throw new SQLException("Falha ao realizar pagamento!!!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public Payment update(Connection conn, Payment obj) {
        return null;
    }

    @Override
    public Payment updateWithUser(Connection conn, Payment obj) {
        return null;
    }

    @Override
    public void delete(Connection conn, Long id) {
    }

    @Override
    public void deleteWithUser(User user, Connection conn, Long id) {
    }

    @Override
    public Payment objectRelationalMapping(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        LocalDateTime moment = rs.getTimestamp("moment").toLocalDateTime();
        int paymentMethodCode = rs.getInt("payment_method");
        Long orderId = rs.getLong("order_id");

        Payment obj = new Payment();
        obj.setId(id);
        obj.setMoment(moment);
        obj.setPaymentMethod(PaymentMethod.valueOf(paymentMethodCode));

        Order order = new Order();
        order.setId(orderId);

        obj.setOrder(order);
        order.setPayment(obj);

        return obj;
    }

    @Override
    public Payment objectRelationalMapping(ResultSet rs, Set<Payment> objs) throws SQLException {
        return null;
    }
}