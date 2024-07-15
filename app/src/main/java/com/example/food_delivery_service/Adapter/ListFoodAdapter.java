package com.example.food_delivery_service.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.model.SelectedProduct;
import com.example.food_delivery_service.api.model.entity.Product;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.FoodViewHolder> {

    private List<Product> productList;
    private OnItemClickListener listener;

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
        // Pass productList to the constructor of FoodViewHolder
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