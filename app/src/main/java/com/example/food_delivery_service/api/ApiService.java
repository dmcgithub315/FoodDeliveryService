package com.example.food_delivery_service.api;

import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.category.CategoryResponse;
import com.example.food_delivery_service.api.model.dto.order.OrderCreateRequest;
import com.example.food_delivery_service.api.model.dto.product.CreateProductRequest;
import com.example.food_delivery_service.api.model.dto.product.OrdersResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.dto.product.UpdateProductRequest;
import com.example.food_delivery_service.api.model.dto.user.LoginRequest;
import com.example.food_delivery_service.api.model.dto.user.LoginResponse;
import com.example.food_delivery_service.api.model.entity.Order;
import com.example.food_delivery_service.api.model.entity.Product;
import com.example.food_delivery_service.api.model.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("register")
    Call<LoginResponse> register(@Body User request);

    @POST("profile/update")
    Call<LoginResponse> update(@Body User request);

    @GET("get-all")
    Call<ApiResponse<ProductsResponse>> getAllProducts(
            @Query("page") Integer page,
            @Query("limit") Integer limit,
            @Query("sort") String sort,
            @Query("cid") Integer cid
    );

    @GET("get-all")
    Call<ApiResponse<ProductsResponse>> getAllProducts2(
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @GET("product/delete/{id}")
    Call<ApiResponse<Product>> deleteProduct(@Path("id") int id);

    @POST("product/create")
    Call<ApiResponse<Product>> createProduct(@Body CreateProductRequest request);

    @GET("product/search")
    Call<ApiResponse<ProductsResponse>> searchProducts(@Query("k") String name);

    @GET("product/details")
    Call<ApiResponse<Product>> getProductDetails(@Query("id") int id);

    @GET("get-all-categories")
    Call<ApiResponse<CategoryResponse>> getAllCategories();

    @POST("product/update/{id}")
    Call<ApiResponse<Product>> updateProduct(@Path("id") int id, @Body UpdateProductRequest request);

    @POST("order/create")
    Call<ApiResponse<Order>> createOrder(@Body OrderCreateRequest request);

    @GET("order/list/{uid}")
    Call<ApiResponse<OrdersResponse>> getOrder(@Path("uid") int id);



}
