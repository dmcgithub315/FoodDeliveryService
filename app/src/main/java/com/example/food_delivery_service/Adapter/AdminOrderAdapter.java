package com.example.food_delivery_service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_delivery_service.R;
import com.example.food_delivery_service.activity.admin.AdminOrders;
import com.example.food_delivery_service.api.model.entity.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    private AdminOrders adminOrdersActivity;

    public AdminOrderAdapter(AdminOrders adminOrdersActivity, List<Order> orders) {
        this.adminOrdersActivity = adminOrdersActivity;
        this.context = adminOrdersActivity; // Assuming context is needed
        this.orders = orders;
    }

    @NonNull
    @Override
    public AdminOrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_orders_admin, parent, false);
        return new AdminOrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);


        holder.paymentMethodTextView.setText(order.getPaymentMethod());
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy : HH:mm");

        try {
            Date createdAtDate = originalFormat.parse(order.getCreatedAt());
            String formattedCreatedAt = targetFormat.format(createdAtDate);

            holder.orderDateTextView.setText(formattedCreatedAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = numberFormat.format(order.getTotalPrice()) + " VND";
        holder.orderTotalTextView.setText(formattedPrice);
        holder.orderStatusTextView.setText(order.getStatus() == 0 ? "Pending" : "Delivered");
        holder.addressTextView.setText(order.getAddress());
        holder.fullname.setText(order.getUser().getFirstName() + " " + order.getUser().getLastName());
        Glide.with(context).load(order.getUser().getAvatar()).
                placeholder(R.drawable.profile_icon).error(R.drawable.profile_icon).circleCrop().into(holder.userImageView);
        holder.phone.setText(order.getUser().getPhone());
        OrderDetailAdapter adapter = new OrderDetailAdapter(context, order.getOrderDetails());
        holder.orderDetailsRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.orderDetailsRecyclerView.setLayoutManager(layoutManager);
        if (order.getStatus() == 1) {
            holder.btnComplete.setVisibility(View.GONE);
        } else {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adminOrdersActivity.handleOrderComplete(order);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDateTextView;
        TextView orderTotalTextView;
        TextView orderStatusTextView;
        TextView paymentMethodTextView;
        TextView addressTextView;
        Button btnComplete;
        ImageView userImageView;
        TextView fullname;
        TextView phone;

        RecyclerView orderDetailsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDateTextView = itemView.findViewById(R.id.cartItem_name);
            orderTotalTextView = itemView.findViewById(R.id.detail_three);
            orderStatusTextView = itemView.findViewById(R.id.detail_one);
            paymentMethodTextView = itemView.findViewById(R.id.detail_two);
            addressTextView = itemView.findViewById(R.id.new_textView_below_detailsLayout);
            orderDetailsRecyclerView = itemView.findViewById(R.id.cartItem_recyclerView);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            userImageView = itemView.findViewById(R.id.cartItem_image);
            fullname = itemView.findViewById(R.id.fullname);
            phone = itemView.findViewById(R.id.phone);

        }
    }

}
