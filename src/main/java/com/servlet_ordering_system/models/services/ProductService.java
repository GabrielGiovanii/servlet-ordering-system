package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.OrderDAO;
import com.servlet_ordering_system.models.daos.ProductDAO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.vos.Product;

import java.sql.Connection;
import java.util.List;

public class ProductService implements CrudService<Product> {

    private final ProductDAO dao;
    private final Connection conn;

    public ProductService() {
        this.dao = new ProductDAO();
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public List<Product> findAll() {
        return dao.findAll(conn);
    }

    @Override
    public Product findById(Long id) {
        return dao.findById(conn, id);
    }

    @Override
    public Product findByString(String name) {
        return dao.findByString(conn, name);
    }

    @Override
    public Product insert(Product obj) {
        return dao.insert(conn, obj);
    }

    @Override
    public Product update(Product obj) {
        return dao.update(conn, obj);
    }

    @Override
    public void delete(Long id) {
        dao.delete(conn, id);
    }
}
