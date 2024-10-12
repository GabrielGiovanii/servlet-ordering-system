package com.servlet_ordering_system.models.daos.contracts;

import java.sql.Connection;
import java.util.List;

public interface CrudDAO<T> {

    List<T> findAll(Connection conn);

    T findById(Connection conn, Long id);

    T findByName(Connection conn, String name);

    T insert(Connection conn, T obj);

    T update(Connection conn, T obj);

    void delete(Connection conn, Long id);
}
