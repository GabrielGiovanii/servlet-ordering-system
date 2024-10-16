package com.servlet_ordering_system.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servlet_ordering_system.models.dtos.ProductDTO;
import com.servlet_ordering_system.models.services.ProductService;
import com.servlet_ordering_system.models.vos.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.servlet_ordering_system.controllers.exceptions.HandlerExceptionController.handleExceptionResponse;

public class ProductController extends HttpServlet {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductController() {
        this.productService = new ProductService();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            String action = req.getPathInfo();

            if (Objects.isNull(action)) {
                List<Product> products = productService.findAll();
                List<ProductDTO> productDTOs = products.stream()
                        .map(ProductDTO::new)
                        .collect(Collectors.toList());

                resp.getWriter().write(objectMapper.writeValueAsString(productDTOs));
            } else {
                Long id = Long.parseLong(action.substring(1));
                Product product = productService.findById(id);

                if (Objects.nonNull(product)) {
                    ProductDTO productDTO = new ProductDTO(product);
                    resp.getWriter().write(objectMapper.writeValueAsString(productDTO));
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

            ProductDTO productDTO = objectMapper.readValue(req.getInputStream(), ProductDTO.class);
            Product product = productService.dtoToObject(productDTO);
            Product createdProduct = productService.insert(product);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(new ProductDTO(createdProduct)));
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");

            ProductDTO productDTO = objectMapper.readValue(req.getInputStream(), ProductDTO.class);
            Product product = productService.dtoToObject(productDTO);

            Product updatedProduct = productService.update(product);

            if (Objects.nonNull(updatedProduct)) {
                resp.getWriter().write(objectMapper.writeValueAsString(new ProductDTO(updatedProduct)));
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

            productService.delete(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            handleExceptionResponse(e, resp);
        }
    }
}
