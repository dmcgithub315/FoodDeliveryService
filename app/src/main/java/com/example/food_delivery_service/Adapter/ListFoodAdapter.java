package com.example.food_delivery_service.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.common.FoodDetailActivity;
import com.example.food_delivery_service.api.model.SelectedProduct;
import com.example.food_delivery_service.api.model.entity.Product;
import com.example.food_delivery_service.util.SharedPrefUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.FoodViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

    private int userRole;

    public ListFoodAdapter(List<Product> productList, int userRole) {
        this.productList = productList;
        this.userRole = userRole;
    }



    public void clearData() {
        productList.clear();
        notifyDataSetChanged();
    }

    public void updateDataSearch(List<Product> products) {
        productList.clear();
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEditClick(int foodId);
        void onDeleteClick(int position);

        void onImageClick(int foodId);

        void onCategoryClick(int categoryId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewTitle;
        public TextView textViewPrice;
        public TextView textViewCategory;
        public TextView textViewQuantity;
        public ImageView iconEdit;
        public ImageView iconDelete;

        public FoodViewHolder(View itemView, final OnItemClickListener listener, final List<Product> productList) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            textViewTitle = itemView.findViewById(R.id.food_name);
            textViewPrice = itemView.findViewById(R.id.food_price);
            textViewCategory = itemView.findViewById(R.id.food_category);
            textViewQuantity = itemView.findViewById(R.id.food_quantity);

            iconEdit = itemView.findViewById(R.id.edit_icon);
            iconDelete = itemView.findViewById(R.id.delete_icon);

            iconEdit.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Product currentItem = productList.get(getAdapterPosition());
                    SelectedProduct.selectedProductId = currentItem.getId();
                    listener.onEditClick(currentItem.getId());
                }
            });

            iconDelete.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(getAdapterPosition());
                }
            });

            textViewCategory.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Product currentItem = productList.get(getAdapterPosition());
                    listener.onCategoryClick(currentItem.getCategory().getId());
                }
            });

            imageView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    Product currentItem = productList.get(getAdapterPosition());
                    SelectedProduct.selectedProductId = currentItem.getId();
                    listener.onImageClick(currentItem.getId());

                    Intent intent = new Intent(v.getContext(), FoodDetailActivity.class);
                    intent.putExtra("PRODUCT_ID", currentItem.getId());
                    v.getContext().startActivity(intent);
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
        return new FoodViewHolder(v, listener, productList);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Product currentItem = productList.get(position);
        Glide.with(holder.imageView.getContext())
                .load(currentItem.getImage())
                .placeholder(R.drawable.baseline_about_us_24)
                .error(R.drawable.food1)
                .into(holder.imageView);
        holder.textViewTitle.setText(currentItem.getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(currentItem.getPrice()) + " VND";
        holder.textViewPrice.setText(formattedPrice);
        holder.textViewCategory.setText(currentItem.getCategory().getName());
        holder.textViewQuantity.setText(String.valueOf(currentItem.getQuantity()));

        if (userRole == 1) {
            holder.iconEdit.setVisibility(View.VISIBLE);
            holder.iconDelete.setVisibility(View.VISIBLE);
        } else {
            holder.iconEdit.setVisibility(View.GONE);
            holder.iconDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateData(List<Product> newProductList) {
        int startPos = productList.size();
        productList.addAll(newProductList);
        notifyItemRangeInserted(startPos, newProductList.size());
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
    }


}