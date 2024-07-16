package com.example.food_delivery_service.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListFunctionAdmin extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        ArrayList<ListFuncAdmin> funcList = new ArrayList<>();
        funcList.add(new ListFuncAdmin("List Food"));
        funcList.add(new ListFuncAdmin("List Order"));
        funcList.add(new ListFuncAdmin("List User"));

        AdminAdapter adapter = new AdminAdapter(this, funcList, new AdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ListFuncAdmin clickedItem = funcList.get(position);
                switch (clickedItem.getFuncName()) {
                    case "List Food":
                        startActivity(new Intent(ListFunctionAdmin.this, ViewListFood.class));
                        break;
                    case "List Order":
//                        startActivity(new Intent(ListFunctionAdmin.this, ListOrderActivity.class));
                        break;
                    case "List User":
//                        startActivity(new Intent(ListFunctionAdmin.this, ListUserActivity.class));
                        break;
                    default:
                        Toast.makeText(ListFunctionAdmin.this, "Function not recognized", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView recyclerView = findViewById(R.id.movieRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        BottomNavigationView navView = findViewById(R.id.admin_nav_view);
        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(ListFunctionAdmin.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}