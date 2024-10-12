package com.servlet_ordering_system.dtos;

import com.servlet_ordering_system.models.vos.Category;
import com.servlet_ordering_system.models.vos.Product;

import java.util.Objects;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Long categoryId;

    public ProductDTO() {
    }

    public ProductDTO(Product obj) {
        this.id = obj.getId();
        this.name = obj.getName();
        this.description = obj.getDescription();
        this.price = obj.getPrice();
        this.imgUrl = obj.getImgUrl();
        this.categoryId = Objects.requireNonNull(obj.getCategory()).getId();
    }

    public Product dtoToObject(ProductDTO dto) {
        Product obj = new Product();
        obj.setId(dto.getId());
        obj.setName(dto.getName());
        obj.setDescription(dto.getDescription());
        obj.setPrice(dto.getPrice());
        obj.setImgUrl(dto.getImgUrl());

        Category category = new Category();
        category.setId(Objects.requireNonNull(dto).getId());

        obj.setCategory(category);
        category.getProducts().add(obj);

        return obj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
