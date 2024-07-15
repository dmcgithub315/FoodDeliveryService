package com.example.food_delivery_service.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.ListCategoryAdapter;
import com.example.food_delivery_service.Adapter.PopularItemsAdapter;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.admin.CreateNewProduct;
import com.example.food_delivery_service.activity.admin.ViewListFood;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.category.CategoryResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.entity.Category;
import com.example.food_delivery_service.api.model.entity.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView categoriesRecyclerView;
    private ListCategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fetchCategories();

        RecyclerView recyclerViewPopular = findViewById(R.id.viewPopular);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PopularItemsAdapter popularItemsAdapter = new PopularItemsAdapter(this, new ArrayList<>());
        recyclerViewPopular.setAdapter(popularItemsAdapter);

        getAllProducts(popularItemsAdapter);
        TextView seeAllTextView = findViewById(R.id.textView15);
        seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ViewListFood.class);
                startActivity(intent);
            }
        });
    }

    private void fetchCategories() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<CategoryResponse>> call = apiService.getAllCategories();

        call.enqueue(new Callback<ApiResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryResponse>> call, Response<ApiResponse<CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData().getCategories();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(HomeActivity.this, android.R.layout.simple_spinner_item, categories) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (view instanceof TextView) {
                                TextView textView = (TextView) view;
                                Category category = getItem(position);
                                if (category != null) {
                                    textView.setText(category.getName());
                                }
                            }
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            if (view instanceof TextView) {
                                TextView textView = (TextView) view;
                                Category category = getItem(position);
                                if (category != null) {
                                    textView.setText(category.getName());
                                }
                            }
                            return view;
                        }
                    };
                    categoryAdapter = new ListCategoryAdapter(HomeActivity.this, categories);
                    categoriesRecyclerView.setAdapter(categoryAdapter);


                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CategoryResponse>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllProducts(PopularItemsAdapter adapter) {


        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<ProductsResponse>> call = apiService.getAllProducts2(1, 4);

        call.enqueue(new Callback<ApiResponse<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductsResponse>> call, Response<ApiResponse<ProductsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getData().getProducts();
                    adapter.updateData(products);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductsResponse>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Failed to get products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
