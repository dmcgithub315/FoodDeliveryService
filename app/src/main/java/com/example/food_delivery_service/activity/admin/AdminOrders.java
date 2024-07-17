package com.example.food_delivery_service.activity.admin;

import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.AdminOrderAdapter;
import com.example.food_delivery_service.R;
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

public class AdminOrders extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_admin);
        String userString = SharedPrefUtils.getStringData(this, USER);
        if(userString == null || userString.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        Gson gsonU = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
        User user = gsonU.fromJson(userString, User.class);
        fetchAndDisplayOrders();



    }

    private void fetchAndDisplayOrders() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<OrdersResponse>> call = apiService.getAllOrder();
        call.enqueue(new Callback<ApiResponse<OrdersResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrdersResponse>> call, Response<ApiResponse<OrdersResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders =  response.body().getData().getOrders();
                    displayOrders(orders);
                } else {
                    Toast.makeText(AdminOrders.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrdersResponse>> call, Throwable t) {
                Toast.makeText(AdminOrders.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void displayOrders(List<Order> orders) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView3);
        AdminOrderAdapter adapter = new AdminOrderAdapter(this, orders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void handleOrderComplete(Order order) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<Order>> call = apiService.completeOrder(order.getId());
        call.enqueue(new Callback<ApiResponse<Order>>() {
            @Override
            public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminOrders.this, "Order completed", Toast.LENGTH_SHORT).show();
                    fetchAndDisplayOrders();
                } else {
                    Toast.makeText(AdminOrders.this, "Failed to complete order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                Toast.makeText(AdminOrders.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
