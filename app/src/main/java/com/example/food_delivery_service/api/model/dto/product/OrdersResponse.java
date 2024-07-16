package com.example.food_delivery_service.api.model.dto.product;

import com.example.food_delivery_service.api.model.entity.Order;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class OrdersResponse {
    @SerializedName("orders")
    private List<Order> orders;
}
