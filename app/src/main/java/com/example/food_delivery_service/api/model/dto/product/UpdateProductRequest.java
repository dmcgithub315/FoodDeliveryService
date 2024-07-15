package com.example.food_delivery_service.api.model.dto.product;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private String name;
    private int quantity;
    private String description;
    private double price;
    private int categoryId;
    private String image;

}