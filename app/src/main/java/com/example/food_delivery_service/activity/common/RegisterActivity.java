package com.example.food_delivery_service.activity.common;

import static com.example.food_delivery_service.util.SharedPrefUtils.TOKEN;
import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText userNameField;
    private Button btnRegister;
    private TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user_register_screen1);
        initView();
    }

    private void initView() {
        emailField = findViewById(R.id.tieEmail);
        passwordField = findViewById(R.id.tiePassword);
        firstNameField = findViewById(R.id.tieFirstName);
        lastNameField = findViewById(R.id.tieLastName);
        userNameField = findViewById(R.id.tieUseName);
        btnRegister = findViewById(R.id.signup_next_button);
        btnLogin = findViewById(R.id.signup_login_button);

        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    private void registerUser() {
        User request = new User();
        request.setEmail(emailField.getText().toString());
        request.setFirstName(firstNameField.getText().toString());
        request.setLastName(lastNameField.getText().toString());
        request.setUsername(userNameField.getText().toString());
        request.setRole(1);
        request.setPassword(passwordField.getText().toString());

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.register(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String token = loginResponse.getToken();
                        User user = loginResponse.getUser();

                        SharedPrefUtils.saveData(RegisterActivity.this, TOKEN, token);
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
                        String jsonString = gson.toJson(user);
                        SharedPrefUtils.saveData(RegisterActivity.this, USER, jsonString);
                    }

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid login details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Network error
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
