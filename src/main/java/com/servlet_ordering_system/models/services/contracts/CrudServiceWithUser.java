package com.servlet_ordering_system.models.services.contracts;

import com.servlet_ordering_system.models.vos.User;

import java.util.List;

public interface CrudServiceWithUser<T> extends CrudService<T>  {

    List<T> findAllWithUser(User user);

    T findByIdWithUser(User user, Long id);

    T findByStringWithUser(User user, String name);

    T updateWithUser(T obj);

    void deleteWithUser(User user, Long id);
}
