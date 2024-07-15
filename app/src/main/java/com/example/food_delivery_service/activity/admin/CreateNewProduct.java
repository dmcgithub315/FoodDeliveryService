package com.example.food_delivery_service.activity.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.category.CategoryResponse;
import com.example.food_delivery_service.api.model.dto.product.CreateProductRequest;
import com.example.food_delivery_service.api.model.entity.Category;
import com.example.food_delivery_service.api.model.entity.Product;
import com.example.food_delivery_service.util.ImgurUploader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextName, editTextPrice, editTextQuantity, editTextDescription;
    private Spinner spinnerCategory;
    private Button btnSave, btnChooseImage;
    private ImageView imageViewFood;
    private Uri imageUri;
    private int productCategoryId = -1;
    private ImgurUploader imgurUploader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_product);

        initializeUI();
        fetchCategories();
        setupSaveButton();
        setupChooseImageButton();
    }

    private void initializeUI() {
        editTextName = findViewById(R.id.editTextText2);
        editTextPrice = findViewById(R.id.editTextNumber2);
        editTextQuantity = findViewById(R.id.editTextNumber3);
        editTextDescription = findViewById(R.id.editTextTextMultiLine);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.button4);
        btnChooseImage = findViewById(R.id.button3);
        imageViewFood = findViewById(R.id.imageView2);
    }

    private void fetchCategories() {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<CategoryResponse>> call = apiService.getAllCategories();

        call.enqueue(new Callback<ApiResponse<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CategoryResponse>> call, Response<ApiResponse<CategoryResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getData().getCategories();
                    ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(CreateNewProduct.this, android.R.layout.simple_spinner_item, categories) {
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
                Toast.makeText(CreateNewProduct.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupChooseImageButton() {
        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewFood.setImageURI(imageUri);
        }
    }

    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {
            try {
                createProduct();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createProduct() throws FileNotFoundException {
        String name = editTextName.getText().toString();
        double price = Double.parseDouble(editTextPrice.getText().toString());
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        String description = editTextDescription.getText().toString();
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryId = selectedCategory.getId();

        CreateProductRequest request = new CreateProductRequest();
        request.setName(name);
        request.setPrice(price);
        request.setQuantity(quantity);
        request.setDescription(description);
        request.setCategory_id(categoryId);

        if (imageUri != null) {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            try{
                imageUri = imgurUploader.uploadImage(imageStream);
                request.setImage(imageUri.toString());
                sendCreateProductRequest(request);
            } catch (Exception e) {
                Toast.makeText(CreateNewProduct.this, "Failed to upload image to IMGUR", Toast.LENGTH_SHORT).show();
                request.setImage(imageUri.toString());
                sendCreateProductRequest(request);
            }

        }


    }

    private void sendCreateProductRequest(CreateProductRequest request) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<Product>> call = apiService.createProduct(request);

        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CreateNewProduct.this, "Product created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNewProduct.this, ViewListFood.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CreateNewProduct.this, "Failed to create product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(CreateNewProduct.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}