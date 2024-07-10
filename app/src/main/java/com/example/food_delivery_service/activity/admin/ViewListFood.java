package com.example.food_delivery_service.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.SearchView;
import android.widget.Toast;

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
    private SearchView searchView;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_food);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ListFoodAdapter(new ArrayList<>());
        recyclerView.setAdapter(productAdapter);

        searchView = findViewById(R.id.search);
        setupSearchView();

        getAllProducts();

        productAdapter.setOnItemClickListener(new ListFoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int foodId) {
                Intent intent = new Intent(ViewListFood.this, UpdateFood.class);
                intent.putExtra("foodId", foodId);
                startActivity(intent);            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete click if needed
            }

        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> searchProducts(newText);
                searchHandler.postDelayed(searchRunnable, 1000);
                return true;
            }
        });
    }

    private void searchProducts(String query) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<ProductsResponse>> call = apiService.searchProducts(query);

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
                // Show an error message to the user
                Toast.makeText(ViewListFood.this, "Failed to search products", Toast.LENGTH_SHORT).show();



            }
        });
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
                Toast.makeText(ViewListFood.this, "Failed to get products", Toast.LENGTH_SHORT).show();
            }
        });
    }


}