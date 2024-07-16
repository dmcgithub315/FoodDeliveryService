package com.example.food_delivery_service.api.model.dto.order;

import com.example.food_delivery_service.api.model.entity.Order;
import com.example.food_delivery_service.api.model.entity.OrderDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private int user_id;

    private int total_price;

    private int status;

    private String payment_method;

    private String order_details;
}
