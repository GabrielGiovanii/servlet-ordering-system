package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAO;
import com.servlet_ordering_system.models.vos.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements CrudDAO<Category> {

    @Override
    public List<Category> findAll(Connection conn) {
        List<Category> categories = new ArrayList<>();

        String commandSql = """
                SELECT id, name FROM tb_category\s
                ORDER BY id;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ResultSet rs =  ps.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");

                Category category = new Category(id, name);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public Category findById(Connection conn, Long id) {
        Category category = null;

        String commandSql = """
                SELECT id, name FROM tb_category\s
                WHERE id = ?\s
                ORDER BY name;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");

                category = new Category(id, name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category findByName(Connection conn, String name) {
        Category category = null;

        String commandSql = """
                SELECT id, name FROM tb_category\s
                WHERE name = ?\s
                ORDER BY name;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, name);

            ResultSet rs =  ps.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");

                category = new Category(id, name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category insert(Connection conn, Category obj) {
        Category category = null;

        String commandSql = """
            INSERT INTO tb_category (name)\s
            VALUES (?);
            """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        category = new Category(id, obj.getName());
                    }
                }
            } else {
                throw new SQLException("Atenção!!! Falha ao inserir registro.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public Category update(Connection conn, Category obj) {
        Category category = null;

        String commandSql = """
            UPDATE tb_category\s
            SET name = ?\s
            WHERE id = ?;
            """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, obj.getName());
            ps.setLong(2, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                category = new Category(obj.getId(), obj.getName());
            } else {
                throw new SQLException("Atenção!!! Falha ao atualizar o registro.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public void delete(Connection conn, Long id) {
        String commandSql = """
            DELETE FROM tb_category\s
            WHERE id = ?;
            """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Atenção!!! Nenhuma categoria encontrada com o ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
