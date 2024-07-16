package com.example.food_delivery_service.api.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Order {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("total_price")
    private int totalPrice;

    @SerializedName("status")
    private boolean status;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    private List<OrderDetail> orderDetails;
}
