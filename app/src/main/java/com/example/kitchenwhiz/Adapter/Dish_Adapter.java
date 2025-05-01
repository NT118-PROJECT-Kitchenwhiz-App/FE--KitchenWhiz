package com.example.kitchenwhiz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kitchenwhiz.R;

import java.util.ArrayList;
import java.util.List;

public class Dish_Adapter extends ArrayAdapter<Dish> {
    private  Context ct;
    private ArrayList<Dish> arr;
    public Dish_Adapter(@NonNull Context context, int resource, @NonNull List<Dish> object){
        super(context, resource, object);
        ct = context;
        arr = new ArrayList<>(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.dish_item, null);
        }
        if (arr.size()>0){
            Dish d = arr.get(position);
        }
        return convertView;
    }
}
