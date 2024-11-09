package com.servlet_ordering_system.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.controllers.contracts.AuthenticatedUserBinder;
import com.servlet_ordering_system.controllers.exceptions.UnauthorizedException;
import com.servlet_ordering_system.models.dtos.UserResponseDTO;
import com.servlet_ordering_system.models.dtos.UserSaveDTO;
import com.servlet_ordering_system.models.services.UserService;
import com.servlet_ordering_system.models.vos.User;
import com.servlet_ordering_system.models.vos.enums.Role;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class UserController extends HttpServlet implements AuthenticatedUserBinder<User> {

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

            User authenticatedUser = getAuthenticatedUser(req);

            if (Objects.isNull(action)) {
                List<User> users = null;

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    users = userService.findAll();
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    throw new UnauthorizedException("CLIENT não pode consultar todos usuários!!!");
                }

                List<UserResponseDTO> userResponseDTOS = Objects.requireNonNull(users).stream()
                        .map(UserResponseDTO::new)
                        .collect(Collectors.toList());

                resp.getWriter().write(objectMapper.writeValueAsString(userResponseDTOS));
            } else {
                User user = null;
                Long id = Long.parseLong(action.substring(1));

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    user =  userService.findById(id);
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    if (!authenticatedUser.getId().equals(id)) {
                        throw new UnauthorizedException("Identificador do CLIENT diferente da respectiva sessão!!!");
                    }

                    user = userService.findById(id);
                }

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
            User user = userService.dtoToObject(userSaveDTO);

            if (user.getRole().equals(Role.ADMIN)) {
                User authenticatedUser = getAuthenticatedUser(req);

                if (Objects.isNull(authenticatedUser) || !authenticatedUser.getRole().equals(Role.ADMIN)) {
                    throw new UnauthorizedException("Somente um ADMIN pode cadastrar outro ADMIN!!!");
                }
            }

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
            User user = userService.dtoToObject(userSaveDTO);

            User authenticatedUser = getAuthenticatedUser(req);

            if (!authenticatedUser.getId().equals(user.getId())) {
                throw new UnauthorizedException("Identificador do USER diferente da respectiva sessão!!!");
            }

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

            User authenticatedUser = getAuthenticatedUser(req);

            if (!authenticatedUser.getId().equals(id)) {
                throw new UnauthorizedException("Identificador do USER diferente da respectiva sessão!!!");
            }

            userService.delete(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    public User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession currentSession = req.getSession(false);

        User authenticatedUser = null;

        if (Objects.nonNull(currentSession)) {
            authenticatedUser = (User) currentSession.getAttribute("authenticatedUser");
        }

        return authenticatedUser;
    }

    @Override
    public void associateUserWithEntity(HttpServletRequest req, User obj) {
    }
}
