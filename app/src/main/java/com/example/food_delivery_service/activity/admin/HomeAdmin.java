package com.example.food_delivery_service.activity.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_delivery_service.R;

public class HomeAdmin extends AppCompatActivity {
    ListFuncAdmin listfunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);

    }
}