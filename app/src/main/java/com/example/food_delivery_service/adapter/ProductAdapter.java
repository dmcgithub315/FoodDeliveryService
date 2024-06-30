package com.example.food_delivery_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.model.entity.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, R.layout.layout_food_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_food_item, parent, false);
        }

        Product product = products.get(position);

        TextView foodName = convertView.findViewById(R.id.food_name);
        foodName.setText(product.getName());


        return convertView;
    }
}