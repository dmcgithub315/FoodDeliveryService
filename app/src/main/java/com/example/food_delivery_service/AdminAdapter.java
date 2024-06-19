package com.example.food_delivery_service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends ArrayAdapter<ListFuncAdmin> {

    private Context ct;
    private ArrayList<ListFuncAdmin> arr;

    public AdminAdapter(@NonNull Context context, int resource, @NonNull List<ListFuncAdmin> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater i = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.layout_list_func_admin, null);
        }
        if (arr.size() > 0){
            ListFuncAdmin l = arr.get(position);
        }
        return convertView;
    }
}
