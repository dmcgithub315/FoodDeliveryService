package com.example.food_delivery_service.activity.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import com.example.food_delivery_service.api.model.dto.category.CategoryResponse;
import com.example.food_delivery_service.api.model.dto.product.UpdateProductRequest;
import com.example.food_delivery_service.api.model.entity.Category;
import com.example.food_delivery_service.api.model.entity.Product;
import com.example.food_delivery_service.util.ImgurUploader;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFood extends AppCompatActivity {

    private EditText editTextName, editTextQuality, editTextPrice, editTextDetail, editTextCategory, editTextQuantity, editTextCreateAt, editTextUpdateAt, editTextId;
    private Button buttonSave, buttonChoose;
    private ImageView imageViewFood;
    private int productCategoryId = -1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImgurUploader imgurUploader;
    private String photoUri;



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
        editTextId = findViewById(R.id.editTextText3);
        editTextName = findViewById(R.id.editTextText2);
        editTextQuality = findViewById(R.id.editTextNumber3);
        editTextPrice = findViewById(R.id.editTextNumber2);
        editTextDetail = findViewById(R.id.editTextTextMultiLine);
        editTextQuantity = findViewById(R.id.editTextNumber3);
        editTextCreateAt = findViewById(R.id.editTextText5);
        editTextUpdateAt = findViewById(R.id.editTextText6);
        buttonSave = findViewById(R.id.button4);
        buttonChoose = findViewById(R.id.button3);
        imageViewFood = findViewById(R.id.imageView2);



        int foodId = SelectedProduct.selectedProductId;
        if (foodId != -1) {
            fetchProductDetails(foodId);
            fetchCategories();
        } else {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show();
        }
        buttonChoose.setOnClickListener(v -> openFileChooser());
        buttonSave.setOnClickListener(v -> updateProduct());



    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageViewFood);
            photoUri = imageUri.toString();
        }
    }





    private void fetchCategories() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<CategoryResponse>> call = apiService.getAllCategories();

        call.enqueue(new Callback<ApiResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryResponse>> call, Response<ApiResponse<CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData().getCategories();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(UpdateFood.this, android.R.layout.simple_spinner_item, categories) {
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
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
                    spinnerCategory.setAdapter(adapter);

                    if (productCategoryId != -1) {
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getId() == productCategoryId) {
                                spinnerCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CategoryResponse>> call, Throwable t) {
                Toast.makeText(UpdateFood.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
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
                    editTextId.setEnabled(false);
                    editTextId.setText(String.valueOf(product.getId()));
                    editTextName.setText(product.getName());
                    editTextQuality.setText(String.valueOf(product.getQuantity()));
                    editTextPrice.setText(String.valueOf(product.getPrice()));
                    editTextDetail.setText(product.getDescription());
                    editTextQuantity.setText(String.valueOf(product.getQuantity()));
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm:ss");

                    try {
                        Date createdAtDate = originalFormat.parse(product.getCreatedAt());
                        Date updatedAtDate = originalFormat.parse(product.getUpdatedAt());
                        String formattedCreatedAt = targetFormat.format(createdAtDate);
                        String formattedUpdatedAt = targetFormat.format(updatedAtDate);

                        editTextCreateAt.setText(formattedCreatedAt);
                        editTextUpdateAt.setText(formattedUpdatedAt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    editTextCreateAt.setEnabled(false);
                    editTextUpdateAt.setEnabled(false);
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

    private void updateProduct() {
        final String[] imageUrl = {null};
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        int productId = Integer.parseInt(editTextId.getText().toString());
        String name = editTextName.getText().toString();
        int quantity = Integer.parseInt(editTextQuality.getText().toString());
        double price = Double.parseDouble(editTextPrice.getText().toString());
        String description = editTextDetail.getText().toString();
        int categoryId = ((Category) ((Spinner) findViewById(R.id.spinnerCategory)).getSelectedItem()).getId();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                if (photoUri != null) {
                    InputStream imageStream = getContentResolver().openInputStream(Uri.parse(photoUri));
                    try {
                        imageUrl[0] = ImgurUploader.uploadToImgur(imageStream);
                    } catch (Exception e) {
                        imageUrl[0] = photoUri;
                    }
                } else {
                    imageUrl[0] = "default_or_existing_image_url";
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                Toast.makeText(UpdateFood.this, "Proceeding with product update", Toast.LENGTH_SHORT).show();
                UpdateProductRequest request = new UpdateProductRequest();
                request.setName(name);
                request.setQuantity(quantity);
                request.setPrice(price);
                request.setDescription(description);
                request.setCategoryId(categoryId);
                request.setImage(imageUrl[0]); // imageUrl[0] now contains either the Imgur URL or the photoUri
                Call<ApiResponse<Product>> call = apiService.updateProduct(productId, request);

                call.enqueue(new Callback<ApiResponse<Product>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(UpdateFood.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateFood.this, ViewListFood.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                        Toast.makeText(UpdateFood.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }
}