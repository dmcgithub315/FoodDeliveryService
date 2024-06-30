package com.example.food_delivery_service.activity.common;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.adapter.ProductAdapter;
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
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_list_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.listfood);
        productAdapter = new ProductAdapter(this, new ArrayList<>());
        listView.setAdapter(productAdapter);

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
                    productAdapter.clear();
                    productAdapter.addAll(products);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductsResponse>> call, Throwable t) {
                Toast.makeText(ViewListFood.this, "Failed to get products", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}