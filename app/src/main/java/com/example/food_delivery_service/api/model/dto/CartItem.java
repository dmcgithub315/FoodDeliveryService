package com.example.food_delivery_service.api.model.dto;

import com.example.food_delivery_service.api.model.entity.Product;

import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
