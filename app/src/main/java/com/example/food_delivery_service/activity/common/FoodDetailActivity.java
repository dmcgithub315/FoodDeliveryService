package com.example.food_delivery_service.activity.common;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.SelectedProduct;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.entity.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailActivity extends AppCompatActivity {
    private TextView FoodDetail_addToCartBtn;
    private TextView FoodDetail_TitleTxt, FoodDetail_PriceTxt, FoodDetail_Desc, numberOrderTxt;
    private ImageView iv_minus, iv_add, FoodDetail_Img;
    private int productCategoryId = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);

        FoodDetail_TitleTxt = findViewById(R.id.FoodDetail_TitleTxt);
        FoodDetail_PriceTxt = findViewById(R.id.FoodDetail_PriceTxt);
        FoodDetail_Desc = findViewById(R.id.FoodDetail_Desc);
        numberOrderTxt = findViewById(R.id.numberOrderTxt);
        iv_minus = findViewById(R.id.iv_minus);
        iv_add = findViewById(R.id.iv_add);
        FoodDetail_Img = findViewById(R.id.FoodDetail_Img);

        FoodDetail_addToCartBtn = findViewById(R.id.FoodDetail_addToCartBtn);
        FoodDetail_addToCartBtn.setOnClickListener(v -> {
        });

        int foodId = SelectedProduct.selectedProductId;
        if (foodId != -1) {
            fetchProductDetails(foodId);
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show();
        }



    }

    private void fetchProductDetails(int productId) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<Product>> call = apiService.getProductDetails(productId);

        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body().getData();
                    productCategoryId = product.getCategory().getId();
                    FoodDetail_TitleTxt.setText(product.getName());
                    FoodDetail_PriceTxt.setText(String.valueOf(product.getPrice()));
                    FoodDetail_Desc.setText(product.getDescription());

                    Glide.with(FoodDetailActivity.this)
                            .load(product.getImage())
                            .placeholder(R.drawable.baseline_about_us_24)
                            .error(R.drawable.food1)
                            .into(FoodDetail_Img);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(FoodDetailActivity.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
