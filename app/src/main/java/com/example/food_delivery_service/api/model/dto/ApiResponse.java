package com.example.food_delivery_service.api.model.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ApiResponse<T> {
    @SerializedName("result")
    private String result;

    @SerializedName("msg")
    private String message;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private T data;

    @SerializedName("paging")
    private Paging paging;
}