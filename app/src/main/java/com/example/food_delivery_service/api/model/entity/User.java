// User.java
package com.example.food_delivery_service.api.model.entity;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import lombok.*;

@Data
public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("active")
    private int active;

    @SerializedName("email")
    @Nullable
    private String email;

    @SerializedName("username")
    @Nullable
    private String username;

    @SerializedName("avatar")
    @Nullable
    private String avatar;

    @SerializedName("updated_at")
    @Nullable
    private String updatedAt;

    @SerializedName("created_at")
    @Nullable
    private String createdAt;

    @SerializedName("first_name")
    @Nullable
    private String firstName;

    @SerializedName("last_name")
    @Nullable
    private String lastName;

    @SerializedName("email_verified")
    @Nullable
    private String emailVerified;

    @SerializedName("role")
    private int role;

    @SerializedName("salt")
    @Nullable
    private String salt;

    @SerializedName("phone")
    @Nullable
    private String phone;


}