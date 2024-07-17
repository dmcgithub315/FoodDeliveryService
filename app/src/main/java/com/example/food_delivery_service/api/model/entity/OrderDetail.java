package com.example.food_delivery_service.api.model.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OrderDetail {
    @SerializedName("id")
    private int id;

    @SerializedName("order_id")
    private int orderId;

    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price")
    private int price;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("product")
    private Product product;


}
