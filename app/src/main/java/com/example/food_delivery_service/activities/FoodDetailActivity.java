package com.example.food_delivery_service.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FoodDetailActivity extends AppCompatActivity {
    private TextView FoodDetail_addToCartBtn;
    private TextView FoodDetail_TitleTxt, FoodDetail_PriceTxt, FoodDetail_Desc, numberOrderTxt;
    private ImageView iv_minus, iv_add, FoodDetail_Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_food_detail);
    }


}
