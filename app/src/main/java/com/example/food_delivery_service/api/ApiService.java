package com.example.food_delivery_service.api;

import com.example.food_delivery_service.api.model.dto.LoginRequest;
import com.example.food_delivery_service.api.model.dto.LoginResponse;
import com.example.food_delivery_service.api.model.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
