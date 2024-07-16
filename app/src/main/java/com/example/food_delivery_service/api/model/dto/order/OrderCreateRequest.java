package com.example.food_delivery_service.api.model.dto.order;

import com.example.food_delivery_service.api.model.entity.Order;
import com.example.food_delivery_service.api.model.entity.OrderDetail;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private Order order;
    private List<OrderDetail> orderDetails;
}
