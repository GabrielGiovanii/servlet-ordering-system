package com.servlet_ordering_system.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.models.services.CategoryService;
import com.servlet_ordering_system.models.vos.Category;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class CategoryController extends HttpServlet {

    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    public CategoryController() {
        this.categoryService = new CategoryService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            String action = req.getPathInfo();

            if (Objects.isNull(action)) {
                List<Category> categories = categoryService.findAll();

                resp.getWriter().write(objectMapper.writeValueAsString(categories));
            } else {
                Long id = Long.parseLong(action.substring(1));

                Category category = categoryService.findById(id);

                if (Objects.nonNull(category)) {
                    resp.getWriter().write(objectMapper.writeValueAsString(category));
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

            Category category = objectMapper.readValue(req.getInputStream(), Category.class);
            Category createdCategory = categoryService.insert(category);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(createdCategory));
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            Category category = objectMapper.readValue(req.getInputStream(), Category.class);

            Category updatedCategory = categoryService.update(category);

            if (updatedCategory != null) {
                resp.getWriter().write(objectMapper.writeValueAsString(updatedCategory));
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

            categoryService.delete(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }
}