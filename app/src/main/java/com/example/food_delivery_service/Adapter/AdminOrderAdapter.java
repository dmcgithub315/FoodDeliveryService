package com.example.food_delivery_service.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.R;
import com.example.food_delivery_service.api.model.entity.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    public AdminOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_orders_admin, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
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

        OrderDetailAdapter adapter = new OrderDetailAdapter(context, order.getOrderDetails());
        holder.orderDetailsRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.orderDetailsRecyclerView.setLayoutManager(layoutManager);
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

        RecyclerView orderDetailsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDateTextView = itemView.findViewById(R.id.cartItem_name);
            orderTotalTextView = itemView.findViewById(R.id.detail_three);
            orderStatusTextView = itemView.findViewById(R.id.detail_one);
            paymentMethodTextView = itemView.findViewById(R.id.detail_two);
            addressTextView = itemView.findViewById(R.id.new_textView_below_detailsLayout);
            orderDetailsRecyclerView = itemView.findViewById(R.id.cartItem_recyclerView);

        }
    }

}
