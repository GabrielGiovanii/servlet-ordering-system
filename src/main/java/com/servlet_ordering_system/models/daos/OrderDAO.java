package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAOWithUser;
import com.servlet_ordering_system.models.daos.contracts.RelationalMapping;
import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.OrderItem;
import com.servlet_ordering_system.models.vos.Product;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.OrderStatus;

import java.sql.*;
import java.util.*;

public class OrderDAO implements CrudDAOWithUser<Order>, RelationalMapping<Order> {

    @Override
    public List<Order> findAll(Connection conn) {
        Set<Order> orders = new HashSet<>();

        String commandSql = """
                SELECT o.id, o.moment, o.client_id, o.order_status,\s
                       p.id AS product_id, p.name AS product_name,\s
                       oi.quantity, oi.price\s
                FROM tb_order o\s
                JOIN tb_order_item oi ON o.id = oi.order_id\s
                JOIN tb_product p ON oi.product_id = p.id\s
                ORDER BY o.id;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(objectRelationalMapping(rs, orders));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders.stream().toList();
    }

    @Override
    public List<Order> findAllWithUser(User user, Connection conn) {
        Set<Order> orders = new HashSet<>();

        String commandSql = """
                SELECT o.id, o.moment, o.client_id, o.order_status,\s
                       p.id AS product_id, p.name AS product_name,\s
                       oi.quantity, oi.price\s
                FROM tb_order o\s
                JOIN tb_order_item oi ON o.id = oi.order_id\s
                JOIN tb_product p ON oi.product_id = p.id\s
                JOIN tb_user u ON o.client_id = u.id\s
                WHERE u.id = ? \s
                ORDER BY o.id;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, user.getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                orders.add(objectRelationalMapping(rs, orders));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders.stream().toList();
    }

    @Override
    public Order findById(Connection conn, Long id) {
        Order order = null;
        Set<Order> orders = new HashSet<>();

        String commandSql = """
                SELECT o.id, o.moment, o.client_id, o.order_status,\s
                       p.id AS product_id, p.name AS product_name,\s
                       oi.quantity, oi.price\s
                FROM tb_order o\s
                JOIN tb_order_item oi ON o.id = oi.order_id\s
                JOIN tb_product p ON oi.product_id = p.id\s
                WHERE o.id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    order = objectRelationalMapping(rs, orders);
                    orders.add(order);
                }

                order = orders.stream().findFirst().orElse(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    @Override
    public Order findByIdWithUser(User user, Connection conn, Long id) {
        Order order = null;
        Set<Order> orders = new HashSet<>();

        String commandSql = """
                SELECT o.id, o.moment, o.client_id, o.order_status,\s
                       p.id AS product_id, p.name AS product_name,\s
                       oi.quantity, oi.price\s
                FROM tb_order o\s
                JOIN tb_order_item oi ON o.id = oi.order_id\s
                JOIN tb_product p ON oi.product_id = p.id\s
                JOIN tb_user u ON o.client_id = u.id\s
                WHERE u.id = ? AND o.id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, user.getId());
            ps.setLong(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    order = objectRelationalMapping(rs, orders);
                    orders.add(order);
                }

                order = orders.stream().findFirst().orElse(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    @Override
    public Order findByString(Connection conn, String string) {
        return null;
    }

    @Override
    public Order findByStringWithUser(User user, Connection conn, String string) {
        return null;
    }

    @Override
    public Order insert(Connection conn, Order obj) {
        String commandSql = """
            INSERT INTO tb_order (client_id, order_status, moment)\s
            VALUES (?, ?, ?);
            """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, Objects.requireNonNull(obj.getClient()).getId());
            ps.setInt(2, Objects.requireNonNull(obj.getOrderStatus()).getCode());
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
                throw new SQLException("Atenção!!! Falha ao inserir o pedido.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    @Override
    public Order update(Connection conn, Order obj) {
        return null;
    }

    @Override
    public Order updateWithUser(Connection conn, Order obj) {
        Order order = null;

        String commandSql = """
            UPDATE tb_order\s
            SET order_status = ?\s
            WHERE client_id = ? AND id = ?;
            """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setInt(1, Objects.requireNonNull(obj.getOrderStatus()).getCode());
            ps.setLong(2, Objects.requireNonNull(obj.getClient()).getId());
            ps.setLong(3, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                order = obj;
            } else {
                throw new SQLException("Atenção!!! Falha ao atualizar o pedido.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    @Override
    public void delete(Connection conn, Long id) {
    }

    @Override
    public void deleteWithUser(User user, Connection conn, Long id) {
    }

    @Override
    public Order objectRelationalMapping(ResultSet rs) {
        return null;
    }

    @Override
    public Order objectRelationalMapping(ResultSet rs, Set<Order> objs) throws SQLException {
        Order obj = null;

        Long id = rs.getLong("id");
        Long productId = rs.getLong("product_id");
        String productName = rs.getString("product_name");
        Integer quantity = rs.getInt("quantity");
        Double price = rs.getDouble("price");

        Product product = new Product();
        product.setId(productId);
        product.setName(productName);

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);

        Optional<Order> foundObject = objs.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();

        if (foundObject.isPresent()) {
            obj = foundObject.get();
        } else {
            Timestamp moment = rs.getTimestamp("moment");
            OrderStatus orderStatus = OrderStatus.valueOf(rs.getInt("order_status"));

            obj = new Order();
            obj.setId(id);
            obj.setOrderStatus(orderStatus);
            obj.setMoment(moment.toLocalDateTime());

            Long clientId = rs.getLong("client_id");

            User user = new User();
            user.setId(clientId);

            user.getOrders().add(obj);
            obj.setClient(user);
        }

        obj.getOrderItems().add(orderItem);
        orderItem.setOrder(obj);
        product.getOrderItems().add(orderItem);
        orderItem.setProduct(product);

        return obj;
    }
}
