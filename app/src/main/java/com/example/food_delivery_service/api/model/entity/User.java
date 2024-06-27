// User.java
package com.example.food_delivery_service.api.model.entity;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("active")
    private int active;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email_verified")
    private String emailVerified;

    @SerializedName("role")
    private int role;

    @SerializedName("salt")
    private String salt;

    @SerializedName("password")
    private String password;



}