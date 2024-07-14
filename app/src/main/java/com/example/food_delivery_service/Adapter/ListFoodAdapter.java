package com.example.food_delivery_service.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food_delivery_service.Model.FoodItem;
import com.example.food_delivery_service.R;

import java.util.List;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.FoodViewHolder> {

    private List<FoodItem> foodList;
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

            iconEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            iconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public ListFoodAdapter(List<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food_item, parent, false);
        return new FoodViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        FoodItem currentItem = foodList.get(position);
        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textViewTitle.setText(currentItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
