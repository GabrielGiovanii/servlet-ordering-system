package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.UserDAO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.security.PasswordUtil;

import java.sql.Connection;
import java.util.List;

public class UserService implements CrudService<User> {

    private final UserDAO dao;
    private final Connection conn;

    public UserService() {
        this.dao = new UserDAO();
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public List<User> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public User findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public User findByName(String name) {
        return dao.findByString(conn, name);
    }

    @Override
    public User insert(User user) {
        String password = PasswordUtil.encryptPassword(user.getPassword());
        user.setPassword(password);

        return dao.insert(conn, user);
    }

    @Override
    public User update(User user) {
        String password = PasswordUtil.encryptPassword(user.getPassword());
        user.setPassword(password);

        return dao.update(conn, user);
    }

    @Override
    public void delete(Long id) {
        dao.delete(conn, id);
    }
}
