package com.example.food_delivery_service.activity.user;

import static com.example.food_delivery_service.util.SharedPrefUtils.TOKEN;
import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.HomeActivity;
import com.example.food_delivery_service.activity.common.LoginActivity;
import com.example.food_delivery_service.activity.common.RegisterActivity;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.user.LoginResponse;
import com.example.food_delivery_service.api.model.entity.User;
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText userNameField;
    private ImageView ivAvatar;
    private Button btnUpdate;
    private Button btnLogout;
    private Button btnFunction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {
        String userString = SharedPrefUtils.getStringData(this, USER);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
        User user = gson.fromJson(userString, User.class);

        emailField = findViewById(R.id.tieEmail);
        firstNameField = findViewById(R.id.tieFirstName);
        lastNameField = findViewById(R.id.tieLastName);
        userNameField = findViewById(R.id.tieUseName);
        btnUpdate = findViewById(R.id.btnUpdate);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnLogout = findViewById(R.id.btnLogout);

        lastNameField.setText(user.getLastName());
        firstNameField.setText(user.getFirstName());
        emailField.setText(user.getEmail());
        userNameField.setText(user.getUsername());
        Glide.with(this).load(user.getAvatar()).into(ivAvatar);

        btnUpdate.setOnClickListener(v -> updateUser(user));
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(ProfileActivity.this, R.style.AlertDialogTheme)
                    .setTitle("Logout")
                    .setMessage("Make you sure ?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        SharedPrefUtils.clearData(this);
                        Intent intent = new Intent(this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });

        btnFunction = findViewById(R.id.btnFunction);
        btnFunction.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout lnHome = findViewById(R.id.lnhome);
        lnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        LinearLayout lnProfile = findViewById(R.id.lnProfile);
        lnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        LinearLayout lnCart = findViewById(R.id.lnCart);
        lnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void updateUser(User user) {
        User request = new User();
        request.setEmail(emailField.getText().toString());
        request.setFirstName(firstNameField.getText().toString());
        request.setLastName(lastNameField.getText().toString());
        request.setUsername(userNameField.getText().toString());
        request.setId(user.getId());

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.update(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        user.setEmail(request.getEmail());
                        user.setFirstName(request.getFirstName());
                        user.setLastName(request.getLastName());
                        user.setUsername(request.getUsername());
                        user.setPhone(request.getPhone());
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
                        String jsonString = gson.toJson(user);
                        SharedPrefUtils.saveData(ProfileActivity.this, USER, jsonString);
                    }
                    Toast.makeText(ProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Network error
                Toast.makeText(ProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
