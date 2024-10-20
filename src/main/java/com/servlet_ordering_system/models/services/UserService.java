package com.servlet_ordering_system.models.services;

import com.servlet_ordering_system.config.database.DatabaseConnection;
import com.servlet_ordering_system.models.daos.UserDAO;
import com.servlet_ordering_system.models.dtos.UserSaveDTO;
import com.servlet_ordering_system.models.services.contracts.CrudService;
import com.servlet_ordering_system.models.services.contracts.DtoConverter;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;
import com.servlet_ordering_system.security.PasswordUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class UserService implements CrudService<User>, DtoConverter<User> {

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
    public User findByString(String name) {
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

    @Override
    public User dtoToObject(Object dto) {
        User obj = new User();

        if (dto instanceof UserSaveDTO userSaveDTO) {
            if (Objects.isNull(userSaveDTO.getRoleId())) {
                throw new IllegalArgumentException("Código da Role é obrigatório!!!");
            }

            obj.setId(userSaveDTO.getId());
            obj.setName(userSaveDTO.getName());
            obj.setEmail(userSaveDTO.getEmail());
            obj.setPhone(userSaveDTO.getPhone());
            obj.setPassword(userSaveDTO.getPassword());
            obj.setRole(Role.valueOf(userSaveDTO.getRoleId()));
        }

        return obj;
    }
}
