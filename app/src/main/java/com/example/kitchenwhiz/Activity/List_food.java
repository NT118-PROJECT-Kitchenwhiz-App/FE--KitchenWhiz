package com.example.kitchenwhiz.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.Adapter.Dish_Adapter;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.R;

import java.util.ArrayList;

public class List_food extends AppCompatActivity {
ListView listFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            listFood = findViewById(R.id.listFood);
            ArrayList<RecipeModel> arrDish = new ArrayList<>();
            while (arrDish.size()<10){
                arrDish.add(new RecipeModel());
            }
            Dish_Adapter Dish_Adapter = new Dish_Adapter(this, 0, arrDish);
            listFood.setAdapter(Dish_Adapter);

            return insets;
        });
    }
}