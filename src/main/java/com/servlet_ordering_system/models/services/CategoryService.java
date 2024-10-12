package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.CategoryDAO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.vos.Category;

import java.sql.Connection;
import java.util.List;

public class CategoryService implements CrudService<Category> {

    private final CategoryDAO dao;
    private final Connection conn;

    public CategoryService() {
        this.dao = new CategoryDAO();
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public List<Category> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public Category findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public Category findByName(String name) {
        return dao.findByName(conn, name);
    }

    @Override
    public Category insert(Category obj) {
        return dao.insert(conn, obj);
    }

    @Override
    public Category update(Category obj) {
        return dao.update(conn, obj);
    }

    @Override
    public void delete(Long id) {
        dao.delete(conn, id);
    }
}
