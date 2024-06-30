package com.example.food_delivery_service.api;

import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.dto.user.LoginRequest;
import com.example.food_delivery_service.api.model.dto.user.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("get-all")
    Call<ApiResponse<ProductsResponse>> getAllProducts();


}
