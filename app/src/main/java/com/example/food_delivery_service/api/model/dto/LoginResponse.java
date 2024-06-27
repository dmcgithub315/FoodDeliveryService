package com.example.food_delivery_service.api.model.dto;

import com.example.food_delivery_service.api.model.entity.User;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;

}
