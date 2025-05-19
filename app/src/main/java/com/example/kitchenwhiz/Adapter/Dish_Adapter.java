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
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class Dish_Adapter extends ArrayAdapter<RecipeModel> {
    private Context ct;
    private int resource;
    private List<RecipeModel> recipeModelsArr;

    public Dish_Adapter(@NonNull Context context, int resource, @NonNull List<RecipeModel> object) {
        super(context, resource, object);
        ct = context;
        this.resource = resource;
        this.recipeModelsArr = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(resource, parent, false);
        }

        TextView title = view.findViewById(R.id.title_dish);
        ShapeableImageView foodImg = view.findViewById(R.id.food_img);
        RecipeModel recipe = recipeModelsArr.get(position);
        title.setText(recipe.getTitle().toUpperCase());

        Glide.with(ct)
                .load(recipe.getImage())
                .placeholder(R.drawable.loading_icon)
                .into(foodImg);

        return view;
    }

    public void updateRecipes(List<RecipeModel> newRecipes) {
        if (recipeModelsArr != null) {
        this.recipeModelsArr.clear(); }
        this.recipeModelsArr.addAll(newRecipes);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView txt_title;
        ImageView image_dish;
    }
}
