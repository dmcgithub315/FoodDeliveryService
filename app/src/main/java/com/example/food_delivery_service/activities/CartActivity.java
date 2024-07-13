package com.example.food_delivery_service.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    private TextView Cart_TitleTxt, Cart_PriceTxt, Cart_Desc, numberOrderTxt;
    private ScrollView scrollView;

    private double tax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cart);


        initView();
    }

    private void initView() {
        /*
        Cart_TitleTxt = findViewById(R.id.Cart_TitleTxt);
        Cart_PriceTxt = findViewById(R.id.Cart_PriceTxt);
        Cart_Desc = findViewById(R.id.Cart_Desc);
        numberOrderTxt = findViewById(R.id.numberOrderTxt);
        scrollView = findViewById(R.id.scrollView);
        recyclerView = findViewById(R.id.recyclerView);
        */

    }

}
