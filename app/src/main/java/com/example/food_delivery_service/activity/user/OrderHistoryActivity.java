package com.example.food_delivery_service.activity.user;

import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.OrderAdapter;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.HomeActivity;
import com.example.food_delivery_service.activity.common.LoginActivity;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.product.OrdersResponse;
import com.example.food_delivery_service.api.model.entity.Order;
import com.example.food_delivery_service.api.model.entity.User;
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);

        String userString = SharedPrefUtils.getStringData(this, USER);
        if(userString == null || userString.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        Gson gsonU = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
        User user = gsonU.fromJson(userString, User.class);
        int userId = user.getId();
        fetchAndDisplayOrders(userId);

        LinearLayout lnHome = findViewById(R.id.lnHome);
        lnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        LinearLayout lnProfile = findViewById(R.id.lnProfile);
        lnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        LinearLayout lnCart = findViewById(R.id.lnCart);
        lnCart.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, CartActivity.class);
            startActivity(intent);
        });


    }

    private void fetchAndDisplayOrders(int userId) {
        Log.d("OrderHistoryActivity", "Fetching orders for user ID: " + userId);
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<OrdersResponse>> call = apiService.getOrder(userId);
        call.enqueue(new Callback<ApiResponse<OrdersResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrdersResponse>> call, Response<ApiResponse<OrdersResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders =  response.body().getData().getOrders();
                    displayOrders(orders);
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrdersResponse>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void displayOrders(List<Order> orders) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        OrderAdapter adapter = new OrderAdapter(this, orders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
