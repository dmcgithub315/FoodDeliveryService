package com.example.food_delivery_service.Model;

public class FoodItem {
    private int imageResource;
    private String title;

    public FoodItem(int imageResource, String title) {
        this.imageResource = imageResource;
        this.title = title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }
}
