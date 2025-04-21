package com.example.kitchenwhiz;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class list_food extends AppCompatActivity {
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
            ArrayList<dish> arrDish = new ArrayList<>();
            while (arrDish.size()<10){
                arrDish.add(new dish());
            }
            dishadapter dishadapter = new dishadapter(this, 0, arrDish);
            listFood.setAdapter(dishadapter);

            return insets;
        });
    }
}