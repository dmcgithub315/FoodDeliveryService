package com.example.food_delivery_service.activity.user;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.adapter.ListFoodAdapter;
import com.example.food_delivery_service.api.model.entity.Product;

public class Dashbroads extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListFoodAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashbroads);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Product> foodList = new ArrayList<>();


//        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ListFoodAdapter(foodList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListFoodAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                // Handle edit click
            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete click
            }
        });
    }
}
