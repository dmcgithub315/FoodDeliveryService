package com.example.food_delivery_service.activity.user;

import static com.example.food_delivery_service.util.SharedPrefUtils.USER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.CartItemAdapter;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.FoodDetailActivity;
import com.example.food_delivery_service.activity.common.HomeActivity;
import com.example.food_delivery_service.activity.common.LoginActivity;
import com.example.food_delivery_service.api.ApiClient;
import com.example.food_delivery_service.api.ApiService;
import com.example.food_delivery_service.api.model.dto.ApiResponse;
import com.example.food_delivery_service.api.model.dto.CartItem;
import com.example.food_delivery_service.api.model.dto.order.OrderCreateRequest;
import com.example.food_delivery_service.api.model.entity.Order;
import com.example.food_delivery_service.api.model.entity.OrderDetail;
import com.example.food_delivery_service.api.model.entity.User;
import com.example.food_delivery_service.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnItemRemovedListener, CartItemAdapter.OnItemClickListener {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    private TextView Cart_TitleTxt, Cart_PriceTxt, Cart_Desc, numberOrderTxt, cart_totalTv, cart_deliveryNumTv, cart_checkoutTv;
    private ScrollView scrollView;

    SharedPreferences sharedPreferences;



    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("CartPreferences", MODE_PRIVATE);


        recyclerView = findViewById(R.id.cart_itemListRV);
        Cart_PriceTxt = findViewById(R.id.cart_priceNumTv);
        cart_totalTv = findViewById(R.id.cart_totalNumTv);
        cart_deliveryNumTv = findViewById(R.id.cart_deliveryNumTv);
        scrollView = findViewById(R.id.cart_scrollView);
        cart_checkoutTv = findViewById(R.id.cart_checkoutTv);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            fetchCartDataAndUpdateUI();
        }

        LinearLayout lnProfile = findViewById(R.id.lnProfile);
        lnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        LinearLayout lnCart = findViewById(R.id.lnCart);
        lnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        LinearLayout lnHome = findViewById(R.id.lnHome);
        lnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        cart_checkoutTv.setOnClickListener(v -> {
            String cartJson = sharedPreferences.getString("cart", "");
            String userString = SharedPrefUtils.getStringData(this, USER);
            if(userString == null || userString.isEmpty()) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            Gson gsonU = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'").create();
            User user = gsonU.fromJson(userString, User.class);

            String totalText = cart_totalTv.getText().toString();
            totalText = totalText.replaceAll("[^\\d]", "");
            int total = Integer.parseInt(totalText);



            if (!cartJson.isEmpty()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<CartItem>>() {}.getType();
                List<CartItem> cartItems = gson.fromJson(cartJson, type);

                Order order = new Order();
                order.setUserId(user.getId());
                order.setTotalPrice(total);
                order.setStatus(0);
                order.setPaymentMethod("COD");

                List<OrderDetail> orderDetails = new ArrayList<>();
                for (CartItem item : cartItems) {
                    OrderDetail detail = new OrderDetail();
                    detail.setProductId(item.getProduct().getId());
                    detail.setQuantity(item.getQuantity());
                    detail.setPrice(item.getProduct().getPrice());
                    orderDetails.add(detail);
                }

                saveOrderAndDetails(order, orderDetails);


            }
        });
    }

    private void saveOrderAndDetails(Order order, List<OrderDetail> orderDetails) {
        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUser_id(order.getUserId());
        request.setTotal_price(order.getTotalPrice());
        request.setStatus(0);
        request.setPayment_method("COD");

        Gson gson = new Gson();
        String json = gson.toJson(orderDetails);
        request.setOrder_details(json);
        Log.d("OrderCreateRequest", json);

        Call<ApiResponse<Order>> call = apiService.createOrder(request);



        call.enqueue(new Callback<ApiResponse<Order>>() {

            @Override
            public void onResponse(@NonNull Call<ApiResponse<Order>> call, @NonNull Response<ApiResponse<Order>> response) {
                if (response.isSuccessful()) {
                    ApiResponse orderCreateResponse = response.body();
                    if (orderCreateResponse.getResult() != null) {
                        Toast.makeText(CartActivity.this, "Create order successfully", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("cart");
                        editor.apply();
                        Intent intent = new Intent(CartActivity.this, OrderHistoryActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    Toast.makeText(CartActivity.this, "Create order fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Create order fail", Toast.LENGTH_SHORT).show();
            }


        }
        );



    }


    @Override
    public void onItemRemoved(int position) {
        List<CartItem> cartItems = ((CartItemAdapter) adapter).cartItems;
        cartItems.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, cartItems.size());
        updateUI(cartItems);

    }

    @Override
    public void onItemClick(int productId) {
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("PRODUCT_ID", productId);
        startActivity(intent);
    }

    private void fetchCartDataAndUpdateUI() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPreferences", MODE_PRIVATE);
        String cartJson = sharedPreferences.getString("cart", "");
        if (!cartJson.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            List<CartItem> cartItems = gson.fromJson(cartJson, type);
            updateUI(cartItems);
        }
    }

    public void updateUI(List<CartItem> cartItems) {
        adapter = new CartItemAdapter(cartItems, this, this);
        recyclerView.setAdapter(adapter);

        int totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        if (Cart_PriceTxt != null) {

            Cart_PriceTxt.setText(formatPrice(totalPrice));
        } else {
            Cart_PriceTxt.setText("0 VND");
        }
        int deliveryFee = 10000;
        int total = totalPrice + deliveryFee;
        cart_totalTv.setText(formatPrice(total));
        cart_deliveryNumTv.setText(formatPrice(deliveryFee));
    }

    @NonNull
    private String formatPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return numberFormat.format(price) + " VND";
    }

}
