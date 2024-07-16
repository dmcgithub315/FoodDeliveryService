package com.example.food_delivery_service.activity.common;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.SelectedProduct;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.CartItem;
import com.example.food_delivery_service.api.model.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailActivity extends AppCompatActivity {
    private TextView FoodDetail_addToCartBtn;
    private TextView FoodDetail_TitleTxt, FoodDetail_PriceTxt, FoodDetail_Desc, numberOrderTxt;
    private ImageView iv_minus, iv_add, FoodDetail_Img;
    private int productCategoryId = -1;
    private Product product2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        ImageView ivAdd = findViewById(R.id.iv_add);
        ImageView ivMinus = findViewById(R.id.iv_minus);
        TextView numberOrderTxt = findViewById(R.id.numberOrderTxt);
        TextView addToCartBtn = findViewById(R.id.FoodDetail_addToCartBtn);
        FoodDetail_TitleTxt = findViewById(R.id.FoodDetail_TitleTxt);
        FoodDetail_PriceTxt = findViewById(R.id.FoodDetail_PriceTxt);
        FoodDetail_Desc = findViewById(R.id.FoodDetail_Desc);
        FoodDetail_Img = findViewById(R.id.FoodDetail_Img);


        fetchProductDetails(getIntent().getIntExtra("PRODUCT_ID", -1));

        ivAdd.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(numberOrderTxt.getText().toString());
            numberOrderTxt.setText(String.valueOf(++currentQuantity));
        });

        ivMinus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(numberOrderTxt.getText().toString());
            if (currentQuantity > 1) {
                numberOrderTxt.setText(String.valueOf(--currentQuantity));
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            int finalQuantity = Integer.parseInt(numberOrderTxt.getText().toString());
            saveToCart(product2, finalQuantity);
        });
    }

    private void saveToCart(Product product, int quantity) {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPreferences", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart", "");
        Gson gson = new Gson();
        List<CartItem> cart;
        if (!cartJson.isEmpty()) {
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            cart = gson.fromJson(cartJson, type);
        } else {
            cart = new ArrayList<>();
        }

        boolean productExists = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            cart.add(new CartItem(product, quantity));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart", gson.toJson(cart));
        editor.apply();

        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
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
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                    String formattedPrice = numberFormat.format(product.getPrice()) + " VND";
                    FoodDetail_PriceTxt.setText(formattedPrice);
                    FoodDetail_Desc.setText(product.getDescription());

                    Glide.with(FoodDetailActivity.this)
                            .load(product.getImage())
                            .placeholder(R.drawable.baseline_about_us_24)
                            .error(R.drawable.food1)
                            .into(FoodDetail_Img);
                    product2 = product;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(FoodDetailActivity.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
