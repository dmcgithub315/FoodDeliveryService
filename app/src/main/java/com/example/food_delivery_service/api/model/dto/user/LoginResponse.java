package com.example.food_delivery_service.api.model.dto.user;

import androidx.annotation.Nullable;

import com.example.food_delivery_service.api.model.entity.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    @Nullable
    private String token;
    @Nullable
    private User user;

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }
}
