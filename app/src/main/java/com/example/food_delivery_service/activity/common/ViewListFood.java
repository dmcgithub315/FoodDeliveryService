package com.example.food_delivery_service.activity.common;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.adapter.ListFoodAdapter;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.entity.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewListFood extends AppCompatActivity {
    private ListFoodAdapter productAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_food);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ListFoodAdapter(new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        getAllProducts();
    }

    private void getAllProducts() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<ProductsResponse>> call = apiService.getAllProducts();

        call.enqueue(new Callback<ApiResponse<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductsResponse>> call, Response<ApiResponse<ProductsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getData().getProducts();
                    productAdapter.updateData(products);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductsResponse>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}