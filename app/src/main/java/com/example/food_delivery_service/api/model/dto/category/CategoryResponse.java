package com.example.food_delivery_service.api.model.dto.category;

import com.example.food_delivery_service.api.model.entity.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {
    @SerializedName("categories")
    private List<Category> categories;
}
