package com.servlet_ordering_system.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.config.JacksonConfig;
import com.servlet_ordering_system.controllers.contracts.AuthenticatedUserBinder;
import com.servlet_ordering_system.controllers.exceptions.UnauthorizedException;
import com.servlet_ordering_system.models.dtos.InsertOrderDTO;
import com.servlet_ordering_system.models.dtos.OrderResponseDTO;
import com.servlet_ordering_system.models.dtos.UpdateOrderDTO;
import com.servlet_ordering_system.models.services.OrderService;
import com.servlet_ordering_system.models.vos.Order;
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

public class OrderController extends HttpServlet implements AuthenticatedUserBinder<Order> {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderController() {
        this.orderService = new OrderService();
        this.objectMapper = JacksonConfig.createObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            String action = req.getPathInfo();

            User authenticatedUser = getAuthenticatedUser(req);

            if (Objects.isNull(action) || action.equals("/")) {
                List<Order> orders = null;

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    orders = orderService.findAll();
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    orders = orderService.findAllWithUser(authenticatedUser);
                }

                List<OrderResponseDTO> orderResponseDTOS = Objects.requireNonNull(orders).stream()
                        .map(OrderResponseDTO::new)
                        .collect(Collectors.toList());

                resp.getWriter().write(objectMapper.writeValueAsString(orderResponseDTOS));
            } else {
                Order order = null;
                Long id = Long.parseLong(action.substring(1));

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    order = orderService.findById(id);
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    order = orderService.findByIdWithUser(authenticatedUser, id);
                }

                if (Objects.nonNull(order)) {
                    resp.getWriter().write(objectMapper.writeValueAsString(new OrderResponseDTO(order)));
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

            InsertOrderDTO insertOrderDTO = objectMapper.readValue(req.getInputStream(), InsertOrderDTO.class);
            Order order = orderService.dtoToObject(insertOrderDTO);

            associateUserWithOrder(req, order);

            Order createdOrder = orderService.insert(order);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(new OrderResponseDTO(createdOrder)));
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            UpdateOrderDTO updateOrderDTO = objectMapper.readValue(req.getInputStream(), UpdateOrderDTO.class);
            Order order = orderService.dtoToObject(updateOrderDTO);

            associateUserWithOrder(req, order);

            Order updatedOrder = orderService.updateWithUser(order);

            if (Objects.nonNull(updatedOrder)) {
                resp.getWriter().write(objectMapper.writeValueAsString(new OrderResponseDTO(updatedOrder)));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    public User getAuthenticatedUser(HttpServletRequest req) {
        HttpSession currentSession = req.getSession(false);
        User authenticatedUser = null;

        authenticatedUser = (User) currentSession.getAttribute("authenticatedUser");

        if (Objects.isNull(authenticatedUser)) {
            throw new UnauthorizedException("Usuário é obrigatório para prosseguir!!!");
        }

        return authenticatedUser;
    }

    @Override
    public void associateUserWithOrder(HttpServletRequest req, Order obj) {
        User authenticatedUser = getAuthenticatedUser(req);

        if (Objects.nonNull(authenticatedUser)) {
            obj.setClient(authenticatedUser);
        }
    }
}
