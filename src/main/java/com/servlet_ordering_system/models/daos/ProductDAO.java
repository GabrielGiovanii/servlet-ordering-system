package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAO;
import com.servlet_ordering_system.models.daos.contracts.RelationalMapping;
import com.servlet_ordering_system.models.vos.Category;
import com.servlet_ordering_system.models.vos.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProductDAO implements CrudDAO<Product>, RelationalMapping<Product> {

    @Override
    public List<Product> findAll(Connection conn) {
        List<Product> products = new ArrayList<>();

        String commandSql = """
                SELECT p.id, p.name, p.description, p.price, p.img_url,\s
                       c.id AS category_id, c.name AS category_name\s
                FROM tb_product p\s
                INNER JOIN tb_category c ON p.category_id = c.id\s
                ORDER BY p.name;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(objectRelationalMapping(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    @Override
    public Product findById(Connection conn, Long id) {
        Product product = null;

        String commandSql = """
                SELECT p.id, p.name, p.description, p.price, p.img_url,\s
                       c.id AS category_id, c.name AS category_name\s
                FROM tb_product p\s
                INNER JOIN tb_category c ON p.category_id = c.id\s
                WHERE p.id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public Product findByString(Connection conn, String string) {
        Product product = null;

        String commandSql = """
                SELECT p.id, p.name, p.description, p.price, p.img_url,\s
                       c.id AS category_id, c.name AS category_name\s
                FROM tb_product p\s
                INNER JOIN tb_category c ON p.category_id = c.id\s
                WHERE p.name = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, string);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public Product insert(Connection conn, Product obj) {
        Product product = null;

        String commandSql = """
                INSERT INTO tb_product (name, description, price, img_url, category_id)\s
                VALUES (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getDescription());
            ps.setDouble(3, obj.getPrice());
            ps.setString(4, obj.getImgUrl());
            ps.setLong(5, Objects.requireNonNull(obj.getCategory()).getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        product = new Product(id, obj.getName(), obj.getDescription(),
                                obj.getPrice(), obj.getImgUrl(), obj.getCategory());
                    }
                }
            } else {
                throw new SQLException("Atenção!!! Falha ao inserir o produto.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public Product update(Connection conn, Product obj) {
        Product product = null;

        String commandSql = """
                UPDATE tb_product\s
                SET name = ?, description = ?, price = ?, img_url = ?, category_id = ?\s
                WHERE id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getDescription());
            ps.setDouble(3, obj.getPrice());
            ps.setString(4, obj.getImgUrl());
            ps.setLong(5, Objects.requireNonNull(obj.getCategory()).getId());
            ps.setLong(6, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                product = new Product(obj.getId(), obj.getName(), obj.getDescription(),
                        obj.getPrice(), obj.getImgUrl(), obj.getCategory());
            } else {
                throw new SQLException("Atenção!!! Falha ao atualizar o produto.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Override
    public void delete(Connection conn, Long id) {
        String commandSql = """
                DELETE FROM tb_product\s
                WHERE id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Atenção!!! Nenhum produto encontrado com o ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product objectRelationalMapping(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Double price = rs.getDouble("price");
        String imgUrl = rs.getString("img_url");

        Long categoryId = rs.getLong("category_id");
        String categoryName = rs.getString("category_name");

        Category category = new Category(categoryId, categoryName);
        return new Product(id, name, description, price, imgUrl, category);
    }

    @Override
    public Product objectRelationalMapping(ResultSet rs, Set<Product> objs) throws SQLException {
        return null;
    }
}
