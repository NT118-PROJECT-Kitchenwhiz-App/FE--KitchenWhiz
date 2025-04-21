package com.example.kitchenwhiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class dishadapter extends ArrayAdapter<dish> {
    private  Context ct;
    private ArrayList<dish> arr;
    public dishadapter(@NonNull Context context, int resource, @NonNull List<dish> object){
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
            dish d = arr.get(position);
        }
        return convertView;
    }
}
