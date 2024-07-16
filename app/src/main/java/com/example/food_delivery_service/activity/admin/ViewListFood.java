package com.example.food_delivery_service.activity.admin;

import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.ListFoodAdapter;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.FoodDetailActivity;
import com.example.food_delivery_service.activity.common.LoginActivity;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.product.ProductsResponse;
import com.example.food_delivery_service.api.model.entity.Product;
import com.example.food_delivery_service.api.model.entity.User;
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private static int currentPage = 1;
    private static int totalPage = 0;
    private boolean isLoading = false;
    private String sort = null;
    private static Integer cid = null;
    private static int count = 0;
    SharedPreferences sharedPreferences;
    int role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_food);

        String userString = SharedPrefUtils.getStringData(this, USER);
        if(userString == null || userString.isEmpty()) {
           role = 0;
        } else {
        Gson gsonU = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
        User user = gsonU.fromJson(userString, User.class);
        role = user.getRole();
        }



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ListFoodAdapter(new ArrayList<>(), role);
        recyclerView.setAdapter(productAdapter);

        searchView = findViewById(R.id.search);
        setupSearchView();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        if (role == 1) {

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(ViewListFood.this, CreateNewProduct.class);
            startActivity(intent);
        });
        } else {
            Button btnCreate = findViewById(R.id.btnCreate);
            btnCreate.setVisibility(View.GONE);

        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1) && !isLoading && currentPage < totalPage) {
                    currentPage++;
                    getAllProducts();
                }

                if (!recyclerView.canScrollVertically(-1) && !isLoading) {
                    count++;
                    if (count == 2) {
                        currentPage = 1;
                        sort = null;
                        cid = null;
                        count=0;
                        getAllProducts();
                    }

                }
            }
        });

        getAllProducts();

        productAdapter.setOnItemClickListener(new ListFoodAdapter.OnItemClickListener() {


            @Override
            public void onEditClick(int foodId) {
                Intent intent = new Intent(ViewListFood.this, UpdateFood.class);
                intent.putExtra("foodId", foodId);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(ViewListFood.this)
                        .setTitle("Delete Product")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            deleteProduct(productAdapter.getProductList().get(position).getId(), position);
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onImageClick(int foodId) {
                Intent intent = new Intent(ViewListFood.this, FoodDetailActivity.class);
                intent.putExtra("foodId", foodId);
                startActivity(intent);
            }

            @Override
            public void onCategoryClick(int categoryId) {
                cid = categoryId;
                currentPage = 1;
                getAllProducts();
            }
        });

        setupSpinner();
    }

    private void deleteProduct(int productId, int position) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<Product>> call = apiService.deleteProduct(productId);

        call.enqueue(new Callback<ApiResponse<Product>>() {
            @Override
            public void onResponse(Call<ApiResponse<Product>> call, Response<ApiResponse<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Product> apiResponse = response.body();
                    if ("OK".equals(apiResponse.getResult()) && "Success".equals(apiResponse.getMessage()) && apiResponse.getCode() == 200) {
                        productAdapter.removeItem(position);
                        Toast.makeText(ViewListFood.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ViewListFood.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewListFood.this, "Failed to delete product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Product>> call, Throwable t) {
                Toast.makeText(ViewListFood.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    productAdapter.updateDataSearch(products);
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
        isLoading = true;
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        Call<ApiResponse<ProductsResponse>> call;

        if (cid == null && sort == null) {
            call = apiService.getAllProducts2(currentPage, 5);
        } else {
            call = apiService.getAllProducts(currentPage, 5, sort, cid);
        }

        call.enqueue(new Callback<ApiResponse<ProductsResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ProductsResponse>> call, Response<ApiResponse<ProductsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getData().getProducts();
                    if (currentPage == 1) {
                        productAdapter.clearData();
                    }

                    productAdapter.updateData(products);
                    currentPage = response.body().getPaging().getCurrent_page();
                    totalPage = response.body().getPaging().getTotal_page();

                    isLoading = false;
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ApiResponse<ProductsResponse>> call, Throwable t) {
                Toast.makeText(ViewListFood.this, "Failed to get products", Toast.LENGTH_SHORT).show();
                isLoading = false;
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void setupSpinner() {
        Spinner sortSpinner = findViewById(R.id.sort_spinner);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortProducts(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void sortProducts(int sortOption) {
        switch (sortOption) {
            case 0:
                sort = "name";
                break;
            case 1:
                sort = "price";
                break;
            case 2:
                sort = "quantity";
                break;
            case 3:
                sort = "id";
                break;
        }
        currentPage = 1;
        getAllProducts();
    }




}