package com.servlet_ordering_system.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.models.dtos.UserResponseDTO;
import com.servlet_ordering_system.models.dtos.UserSaveDTO;
import com.servlet_ordering_system.models.services.UserService;
import com.servlet_ordering_system.models.vos.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class UserController extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserController() {
        this.userService = new UserService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            String action = req.getPathInfo();

            if (Objects.isNull(action)) {
                List<User> users = userService.findAll();
                List<UserResponseDTO> userResponseDTOS = users.stream()
                        .map(UserResponseDTO::new)
                        .collect(Collectors.toList());

                resp.getWriter().write(objectMapper.writeValueAsString(userResponseDTOS));
            } else {
                Long id = Long.parseLong(action.substring(1));
                User user = userService.findById(id);

                if (Objects.nonNull(user)) {
                    UserResponseDTO userResponseDTO = new UserResponseDTO(user);
                    resp.getWriter().write(objectMapper.writeValueAsString(userResponseDTO));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            UserSaveDTO userSaveDTO = objectMapper.readValue(req.getInputStream(), UserSaveDTO.class);
            User user = userSaveDTO.dtoToObject(userSaveDTO);
            User createdUser = userService.insert(user);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(new UserResponseDTO(createdUser)));
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            UserSaveDTO userSaveDTO = objectMapper.readValue(req.getInputStream(), UserSaveDTO.class);
            User user = userSaveDTO.dtoToObject(userSaveDTO);

            User updatedUser = userService.update(user);

            if (Objects.nonNull(updatedUser)) {
                resp.getWriter().write(objectMapper.writeValueAsString(new UserResponseDTO(updatedUser)));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Long id = Long.parseLong(req.getPathInfo().substring(1));

            userService.delete(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }
}
