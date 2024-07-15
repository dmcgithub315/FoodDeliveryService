package com.example.food_delivery_service.api.model.dto;

import lombok.Data;

@Data
public class Paging {
    private int current_page;
    private int total_page;
}
