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
import com.example.food_delivery_service.api.model.entity.OrderDetail;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetails;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderdetail_item_layout, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetails.get(position);
//        holder.productNameTextView.setText(orderDetail.get);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(orderDetail.getPrice()) + " VND";
        holder.productPriceTextView.setText(formattedPrice);
        holder.productQuantityTextView.setText("x" + orderDetail.getQuantity());
        holder.productNameTextView.setText(orderDetail.getProduct().getName());
        Glide.with(context)
                .load(orderDetail.getProduct().getImage())
                .placeholder(R.drawable.food1)
                .error(R.drawable.food1)

                .into(holder.productImageView);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView productPriceTextView;
        public TextView productQuantityTextView;
        public ImageView productImageView;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.Name);
            productPriceTextView = itemView.findViewById(R.id.detail_one);
            productQuantityTextView = itemView.findViewById(R.id.detail_two);
            productImageView = itemView.findViewById(R.id.cartItem_image);
        }
    }

}
