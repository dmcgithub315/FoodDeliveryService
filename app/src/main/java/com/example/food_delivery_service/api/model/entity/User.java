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

    @SerializedName("password")
    @Nullable
    private String password;

    // Getter và Setter

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable String createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Nullable String firstName) {
        this.firstName = firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@Nullable String lastName) {
        this.lastName = lastName;
    }

    @Nullable
    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(@Nullable String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Nullable
    public String getSalt() {
        return salt;
    }

    public void setSalt(@Nullable String salt) {
        this.salt = salt;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }
}
