package com.example.food_delivery_service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.model.entity.Product;

import java.util.List;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.FoodViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public ImageView iconEdit;
        public ImageView iconDelete;

        public FoodViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            textViewTitle = itemView.findViewById(R.id.food_name);
            iconEdit = itemView.findViewById(R.id.edit_icon);
            iconDelete = itemView.findViewById(R.id.delete_icon);

            iconEdit.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEditClick(getAdapterPosition());
                }
            });

            iconDelete.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getAdapterPosition());
                }
            });
        }
    }

    public ListFoodAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food_item, parent, false);
        return new FoodViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Product currentItem = productList.get(position);
        Glide.with(holder.imageView.getContext())
                .load(currentItem.getImage())
                .placeholder(R.drawable.food1)
                .error(R.drawable.baseline_about_us_24)
                .into(holder.imageView);
        holder.textViewTitle.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateData(List<Product> newProductList) {
        productList.clear();
        productList.addAll(newProductList);
        notifyDataSetChanged();
    }
}