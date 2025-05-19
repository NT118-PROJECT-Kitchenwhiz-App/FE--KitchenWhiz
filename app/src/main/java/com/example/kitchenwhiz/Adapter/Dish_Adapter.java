package com.example.kitchenwhiz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kitchenwhiz.Model.RecipeInfo;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.R;

import java.util.ArrayList;
import java.util.List;

public class Dish_Adapter extends ArrayAdapter<RecipeInfo> {
    private Context ct;
    private ArrayList<RecipeModel> recipeModelsArr;

    public Dish_Adapter(@NonNull Context context, int resource, @NonNull List<RecipeInfo> object) {
        super(context, resource, object);
        ct = context;
        recipeModelsArr = new ArrayList<>(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(ct);
            convertView = inflater.inflate(R.layout.dish_item, parent, false);
            holder = new ViewHolder();
            holder.txt_title = convertView.findViewById(R.id.title_dish);
            holder.image_dish = convertView.findViewById(R.id.food_img);
            convertView.setTag(holder);
        }
        if (recipeModelsArr.size() > 0) {
            RecipeModel recipeModel = recipeModelsArr.get(position);
            holder.txt_title.setText(recipeModel.getTitle());
            if (recipeModel.getImage() != null && !recipeModel.getImage().isEmpty()) {
                Glide.with(ct)
                        .load(recipeModel.getImage())
                        .error(Glide.with(ct).load(R.drawable.error))
                        .into(holder.image_dish);
            } else {
                Glide.with(ct)
                        .load(R.drawable.error)
                        .into(holder.image_dish);
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        TextView txt_title;
        ImageView image_dish;
    }
}
