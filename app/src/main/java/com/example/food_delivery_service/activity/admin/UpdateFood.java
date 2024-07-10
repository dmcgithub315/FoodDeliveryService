package com.example.food_delivery_service.activity.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class UpdateFood extends AppCompatActivity {

    private EditText editTextName, editTextQuality, editTextPrice, editTextDetail;
    private Button buttonSave;
    private ImageView imageViewFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        editTextName = findViewById(R.id.editTextText2);
        editTextQuality = findViewById(R.id.editTextNumber);
        editTextPrice = findViewById(R.id.editTextNumber2);
        editTextDetail = findViewById(R.id.editTextTextMultiLine);
        buttonSave = findViewById(R.id.button4);
        imageViewFood = findViewById(R.id.imageView2);


        int foodId = SelectedProduct.selectedProductId; // Use static field instead of intent
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
                    // Update UI with product details
                    editTextName.setText(product.getName());
                    editTextQuality.setText(String.valueOf(1));
                    editTextPrice.setText(String.valueOf(product.getPrice()));
                    editTextDetail.setText(product.getDescription());
                    // Load image
                    Glide.with(UpdateFood.this)
                            .load(product.getImage())
                            .placeholder(R.drawable.food1)
                            .error(R.drawable.baseline_about_us_24)
                            .into(imageViewFood);
                     }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(UpdateFood.this, "Failed to fetch product details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}