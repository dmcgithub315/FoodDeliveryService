package com.example.food_delivery_service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.model.entity.Product;

import java.util.List;

public class PopularItemsAdapter extends RecyclerView.Adapter<PopularItemsAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public PopularItemsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_popular_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewTitle.setText(product.getName());
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        String imageUrl = product.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context).load(imageUrl)
                    .placeholder(R.drawable.food1) // Placeholder image
                    .error(R.drawable.food1) // Error image
                    .into(holder.imageView);
        } else {
            Glide.with(context).load(R.drawable.food1).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewPrice;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.food_name);
            textViewPrice = itemView.findViewById(R.id.food_price);
            imageView = itemView.findViewById(R.id.imageView3);

        }
    }

    public void updateData(List<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
