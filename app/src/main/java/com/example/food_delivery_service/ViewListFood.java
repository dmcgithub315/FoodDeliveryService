package com.example.food_delivery_service;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Adapter.ListFoodAdapter;
import com.example.food_delivery_service.Model.FoodItem;

import java.util.ArrayList;

public class ViewListFood extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListFoodAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_list_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<FoodItem> foodList = new ArrayList<>();
        foodList.add(new FoodItem(R.drawable.food1, "Food Title 1"));
        foodList.add(new FoodItem(R.drawable.food1, "Food Title 2"));
        // Add more items...

        recyclerView = findViewById(R.id.recycler_view);
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
