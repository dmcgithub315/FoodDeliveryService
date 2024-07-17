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
    private int status;

    @SerializedName("payment_method")
    private String paymentMethod;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("address")
    private String address;

    @SerializedName("order_details")
    private List<OrderDetail> orderDetails;

    @SerializedName("user")
    private User user;
}
