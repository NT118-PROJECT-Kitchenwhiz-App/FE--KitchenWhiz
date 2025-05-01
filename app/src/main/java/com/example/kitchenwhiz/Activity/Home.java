package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.R;

public class Home extends AppCompatActivity {
TextView username;
EditText Search;
View Randomfood, Createfood;
ImageView favoriteIcon, recentlyIcon, suggestedIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            return insets;
        });
    }

    private void mapping(){
        username = findViewById(R.id.txtname);
        Search = findViewById(R.id.home_search);
        Randomfood = findViewById(R.id.rcmrandomfood);
        Createfood = findViewById(R.id.createdish);
        favoriteIcon = findViewById(R.id.btnfavorite);
        recentlyIcon = findViewById(R.id.btncooked);
        suggestedIcon = findViewById(R.id.btnsuggested);
    }
}