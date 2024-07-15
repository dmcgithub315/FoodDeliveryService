package com.example.food_delivery_service.api.model.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Product {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("category_id")
    private int categoryId;

    @SerializedName("price")
    private int price;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("image")
    private String image;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("category")
    private Category category;
}
