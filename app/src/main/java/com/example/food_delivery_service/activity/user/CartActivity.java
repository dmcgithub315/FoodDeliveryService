package com.example.food_delivery_service.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.CartItemAdapter;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.FoodDetailActivity;
import com.example.food_delivery_service.activity.common.HomeActivity;
import com.example.food_delivery_service.api.model.dto.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnItemRemovedListener, CartItemAdapter.OnItemClickListener {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    private TextView Cart_TitleTxt, Cart_PriceTxt, Cart_Desc, numberOrderTxt, cart_totalTv, cart_deliveryNumTv;
    private ScrollView scrollView;

    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_itemListRV);
        Cart_PriceTxt = findViewById(R.id.cart_priceNumTv);
        cart_totalTv = findViewById(R.id.cart_totalNumTv);
        cart_deliveryNumTv = findViewById(R.id.cart_deliveryNumTv);
        scrollView = findViewById(R.id.cart_scrollView);

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

    private String formatPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return numberFormat.format(price) + " VND";
    }

}
