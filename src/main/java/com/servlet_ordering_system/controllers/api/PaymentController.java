package com.servlet_ordering_system.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.config.JacksonConfig;
import com.servlet_ordering_system.controllers.contracts.AuthenticatedUserBinder;
import com.servlet_ordering_system.models.dtos.PaymentDTO;
import com.servlet_ordering_system.models.services.PaymentService;
import com.servlet_ordering_system.models.vos.Payment;
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

public class PaymentController extends HttpServlet implements AuthenticatedUserBinder<Payment> {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    public PaymentController() {
        this.paymentService = new PaymentService();
        this.objectMapper = JacksonConfig.createObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            String action = req.getPathInfo();

            User authenticatedUser = getAuthenticatedUser(req);

            if (Objects.isNull(action)) {
                List<Payment> payments = null;

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    payments = paymentService.findAll();
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    payments = paymentService.findAllWithUser(authenticatedUser);
                }

                List<PaymentDTO> paymentDTOS = Objects.requireNonNull(payments).stream()
                        .map(PaymentDTO::new)
                        .collect(Collectors.toList());

                resp.getWriter().write(objectMapper.writeValueAsString(paymentDTOS));
            } else {
                Payment payment = null;
                Long id = Long.parseLong(action.substring(1));

                if (authenticatedUser.getRole().equals(Role.ADMIN)) {
                    payment = paymentService.findById(id);
                } else if (authenticatedUser.getRole().equals(Role.CLIENT)) {
                    payment = paymentService.findByIdWithUser(authenticatedUser, id);
                }

                if (Objects.nonNull(payment)) {
                    resp.getWriter().write(objectMapper.writeValueAsString(new PaymentDTO(payment)));
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

            PaymentDTO paymentDTO = objectMapper.readValue(req.getInputStream(), PaymentDTO.class);
            Payment payment = paymentService.dtoToObject(paymentDTO);

            associateUserWithEntity(req, payment);

            payment = paymentService.insert(payment);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(new PaymentDTO(payment)));
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
    public void associateUserWithEntity(HttpServletRequest req, Payment obj) {
    }
}
