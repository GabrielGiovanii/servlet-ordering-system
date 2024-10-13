package com.servlet_ordering_system.models.daos;

import com.servlet_ordering_system.models.daos.contracts.CrudDAO;
import com.servlet_ordering_system.models.daos.contracts.RelationalMapping;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO implements CrudDAO<User>, RelationalMapping<User> {

    @Override
    public List<User> findAll(Connection conn) {
        List<User> users = new ArrayList<>();

        String commandSql = """
                SELECT u.id, u.name, u.email, u.phone, u.role, u.password\s
                FROM tb_user u\s
                ORDER BY u.name;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(objectRelationalMapping(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public User findById(Connection conn, Long id) {
        User user = null;

        String commandSql = """
                SELECT u.id, u.name, u.email, u.phone, u.role, u.password\s
                FROM tb_user u\s
                WHERE u.id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User findByString(Connection conn, String string) {
        User user = null;

        String commandSql = """
                SELECT u.id, u.name, u.email, u.phone, u.role, u.password\s
                FROM tb_user u\s
                WHERE u.email = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, string);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = objectRelationalMapping(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User insert(Connection conn, User obj) {
        User user = null;

        String commandSql = """
                INSERT INTO tb_user (name, email, phone, password, role)\s
                VALUES (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPhone());
            ps.setString(4, obj.getPassword());
            ps.setInt(5, Objects.requireNonNull(obj.getRole()).getCode());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Long id = generatedKeys.getLong(1);
                        user = new User(id, obj.getName(), obj.getEmail(), obj.getPhone(), obj.getPassword(), obj.getRole());
                    }
                }
            } else {
                throw new SQLException("Atenção!!! Falha ao inserir o usuário.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public User update(Connection conn, User obj) {
        User user = null;

        String commandSql = """
                UPDATE tb_user\s
                SET name = ?, email = ?, phone = ?, password = ?, role = ?\s
                WHERE id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setString(3, obj.getPhone());
            ps.setString(4, obj.getPassword());
            ps.setInt(5, Objects.requireNonNull(obj.getRole()).getCode());
            ps.setLong(6, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 1) {
                user = new User(obj.getId(), obj.getName(), obj.getEmail(), obj.getPhone(), obj.getPassword(), obj.getRole());
            } else {
                throw new SQLException("Atenção!!! Falha ao atualizar o usuário.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void delete(Connection conn, Long id) {
        String commandSql = """
                DELETE FROM tb_user\s
                WHERE id = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(commandSql)) {
            ps.setLong(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Atenção!!! Nenhum usuário encontrado com o ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User objectRelationalMapping(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String password = rs.getString("password");
        Role role = Role.valueOf(rs.getInt("role"));

        return new User(id, name, email, phone, password, role);
    }
}
