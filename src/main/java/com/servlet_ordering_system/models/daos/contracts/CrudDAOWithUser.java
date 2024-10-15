package com.servlet_ordering_system.models.daos.contracts;

import com.servlet_ordering_system.models.vos.User;

import java.sql.Connection;
import java.util.List;

public interface CrudDAOWithUser<T> extends CrudDAO<T> {

    List<T> findAllWithUser(User user, Connection conn);

    T findByIdWithUser(User user, Connection conn, Long id);

    T findByStringWithUser(User user, Connection conn, String string);

    T updateWithUser(User user, Connection conn, T obj);

    void deleteWithUser(User user, Connection conn, Long id);
}
