package com.example.food_delivery_service.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.user.CartActivity;
import com.example.food_delivery_service.api.model.dto.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    public List<CartItem> cartItems;
    private OnItemRemovedListener listener;
    private OnItemClickListener itemClickListener;


    public CartItemAdapter(List<CartItem> cartItems, OnItemRemovedListener removedListener, OnItemClickListener itemClickListener) {
        this.cartItems = cartItems;
        this.listener = removedListener;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
        return new CartItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem currentItem = cartItems.get(position);
        holder.itemName.setText(currentItem.getProduct().getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(currentItem.getProduct().getPrice()) + " VND";
        holder.itemPrice.setText(formattedPrice);
        holder.itemQuantity.setText(String.format("%d", currentItem.getQuantity()));
        Glide.with(holder.itemView.getContext())
                .load(currentItem.getProduct().getImage())
                .placeholder(R.drawable.food1)
                .error(R.drawable.food1)
                .into(holder.itemImage);

        holder.deleteIcon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemRemoved(position);
            }
        });

        holder.itemImage.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentItem.getProduct().getId());
            }
        });

        holder.ivAdd.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantity();
            currentItem.setQuantity(currentQuantity + 1);
            holder.itemQuantity.setText(String.valueOf(currentQuantity + 1));
            notifyItemChanged(position);
            CartActivity cartActivity = (CartActivity) holder.itemView.getContext();
            cartActivity.updateUI(cartItems);
        });

        holder.ivMinus.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantity();
            if (currentQuantity > 1) {
                currentItem.setQuantity(currentQuantity - 1);
                holder.itemQuantity.setText(String.valueOf(currentQuantity - 1));
                notifyItemChanged(position);
                CartActivity cartActivity = (CartActivity) holder.itemView.getContext();
                cartActivity.updateUI(cartItems);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQuantity;
        ImageView itemImage, deleteIcon, ivAdd, ivMinus;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.cartItem_name);
            itemPrice = itemView.findViewById(R.id.cartItem_price);
            itemQuantity = itemView.findViewById(R.id.cartItem_quantity);
            itemImage = itemView.findViewById(R.id.cartItem_image);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
            ivAdd = itemView.findViewById(R.id.iv_add);
            ivMinus = itemView.findViewById(R.id.iv_minus);
        }
    }



    public interface OnItemRemovedListener {
        void onItemRemoved(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int productId);
    }
}