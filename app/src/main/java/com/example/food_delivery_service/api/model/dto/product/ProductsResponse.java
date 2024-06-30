package com.example.food_delivery_service.api.model.dto.product;

import com.example.food_delivery_service.api.model.entity.Product;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ProductsResponse {
    @SerializedName("products")
    private List<Product> products;
}