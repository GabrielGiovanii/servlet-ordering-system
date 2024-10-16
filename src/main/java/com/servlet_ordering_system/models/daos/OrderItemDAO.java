package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAO;
import com.servlet_ordering_system.models.daos.contracts.RelationalMapping;
import com.servlet_ordering_system.models.vos.Order;
import com.servlet_ordering_system.models.vos.OrderItem;
import com.servlet_ordering_system.models.vos.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class OrderItemDAO implements CrudDAO<OrderItem>, RelationalMapping<OrderItem> {

    @Override
    public List<OrderItem> findAll(Connection conn) {
        return null;
    }

    @Override
    public OrderItem findById(Connection conn, Long id) {
        return null;
    }

    @Override
    public OrderItem findByString(Connection conn, String string) {
        return null;
    }

    @Override
    public OrderItem insert(Connection conn, OrderItem obj) {
        OrderItem orderItem = null;

        String commandSql = """
                INSERT INTO tb_order_item (product_id, order_id, quantity, price)\s
                VALUES (?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, Objects.requireNonNull(obj.getProduct()).getId());
            ps.setLong(2, Objects.requireNonNull(obj.getOrder()).getId());
            ps.setInt(3, obj.getQuantity());
            ps.setDouble(4, obj.getPrice());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                orderItem = new OrderItem(obj.getProduct(), obj.getOrder(), obj.getQuantity(), obj.getPrice());
            } else {
                throw new SQLException("Atenção!!! Falha ao inserir o item do pedido.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orderItem;
    }

    @Override
    public OrderItem update(Connection conn, OrderItem obj) {
        return null;
    }

    @Override
    public void delete(Connection conn, Long id) {
    }

    @Override
    public OrderItem objectRelationalMapping(ResultSet rs) throws SQLException {
        Long productId = rs.getLong("product_id");
        Long orderId = rs.getLong("order_id");
        Integer quantity = rs.getInt("quantity");
        Double price = rs.getDouble("price");

        Product product = new Product();
        product.setId(productId);

        Order order = new Order();
        order.setId(orderId);

        OrderItem obj = new OrderItem(product, order, quantity, price);

        product.getOrderItems().add(obj);
        order.getOrderItems().add(obj);

        return obj;
    }

    @Override
    public OrderItem objectRelationalMapping(ResultSet rs, Set<OrderItem> objs) throws SQLException {
        return null;
    }
}
