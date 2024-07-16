package com.example.food_delivery_service.activity.common;

import static com.example.food_delivery_service.util.SharedPrefUtils.TOKEN;
import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.admin.ViewListFood;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.user.LoginRequest;
import com.example.food_delivery_service.api.model.dto.user.LoginResponse;
import com.example.food_delivery_service.api.model.entity.User;
import com.example.food_delivery_service.auth.SessionManager;
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user_main);

        // Get the email and password from the input fields
        EditText emailField = findViewById(R.id.email_login);
        EditText passwordField = findViewById(R.id.password);
        Button signInButton = findViewById(R.id.login_user_main_buttonLogin);
        TextView btnRegister = findViewById(R.id.textView2);

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        signInButton.setOnClickListener(v -> {
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            // Create a new login request
            LoginRequest loginRequest = new LoginRequest(email, password);

            // Get the API service and call the login method
            ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
            Call<LoginResponse> call = apiService.login(loginRequest);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        if (loginResponse != null) {
                            String token = loginResponse.getToken();
                            User user = loginResponse.getUser();

                            SharedPrefUtils.saveData(LoginActivity.this, TOKEN, token);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
                            String jsonString = gson.toJson(user);
                            SharedPrefUtils.saveData(LoginActivity.this, USER, jsonString);
                        }

                        Intent intent = new Intent(LoginActivity.this, ViewListFood.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid login details", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    // Network error
                    Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}