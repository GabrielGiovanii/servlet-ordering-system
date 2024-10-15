package com.servlet_ordering_system.models.services.contracts;

import java.util.List;

public interface CrudService<T> {

    List<T> findAll();

    T findById(Long id);

    T findByString(String name);

    T insert(T obj);

    T update(T obj);

    void delete(Long id);
}
