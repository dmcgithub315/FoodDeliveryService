package com.example.food_delivery_service.api;

import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.dto.user.LoginRequest;
import com.example.food_delivery_service.api.model.dto.user.LoginResponse;
import com.example.food_delivery_service.api.model.entity.Product;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("get-all")
    Call<ApiResponse<ProductsResponse>> getAllProducts();

    @GET("product/search")
    Call<ApiResponse<ProductsResponse>> searchProducts(@Query("k") String name);

    @GET("product/details")
    Call<ApiResponse<Product>> getProductDetails(@Query("id") int id);

}
