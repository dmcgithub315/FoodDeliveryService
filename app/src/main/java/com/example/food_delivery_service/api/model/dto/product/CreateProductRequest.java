package com.example.food_delivery_service.api.model.dto.product;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private int quantity;
    private String description;
    private double price;
    private int category_id;
    private String image;

}
