package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.R;

public class Add_food extends AppCompatActivity {
    ImageView image;
    EditText txtname, txtdes, txtser, txttime,txtins;
    Button btnadd;
    TextView btnaddin;
    LinearLayout ingredientContainer;
    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            addRow();
            addRow();
            btnaddin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRow();
                }
            });
            return insets;
        });
    }

    private void mapping() {
        image = findViewById(R.id.image);
        txtname = findViewById(R.id.txtnamefood);
        txtdes = findViewById(R.id.txtdes);
        txtins = findViewById(R.id.txtins);
        txtser = findViewById(R.id.txtser);
        txttime = findViewById(R.id.txttime);
        btnaddin = findViewById(R.id.addingre);
        btnadd = findViewById(R.id.btnaddfood);
        ingredientContainer = findViewById(R.id.ingredient_container);
    }

    private void addRow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View ingredientRow = inflater.inflate(R.layout.ingredient_rows, ingredientContainer, false);
        ingredientContainer.addView(ingredientRow);
    }


}