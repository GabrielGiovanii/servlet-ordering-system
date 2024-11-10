package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.config.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.ProductDAO;
import com.servlet_ordering_system.models.dtos.ProductDTO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.services.contracts.DtoConverter;
import com.servlet_ordering_system.models.vos.Category;
import com.servlet_ordering_system.models.vos.Product;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class ProductService implements CrudService<Product>, DtoConverter<Product> {

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

    public List<Product> findAllByName(String name) {
        return dao.findAllByName(conn, name);
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

    @Override
    public Product dtoToObject(Object dto) {
        Product obj = new Product();

        if (dto instanceof ProductDTO productDTO) {
            obj.setId(productDTO.getId());
            obj.setName(productDTO.getName());
            obj.setDescription(productDTO.getDescription());
            obj.setPrice(productDTO.getPrice());
            obj.setImgUrl(productDTO.getImgUrl());

            Category category = new Category();
            category.setId(Objects.requireNonNull(productDTO).getId());

            obj.setCategory(category);
            category.getProducts().add(obj);
        }

        return obj;
    }
}
