package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.google.android.material.imageview.ShapeableImageView;

public class Home extends AppCompatActivity {
TextView txtusername;
EditText tbSearch;
ShapeableImageView avatar;
View viewRandomfood, viewCreatefood;
ImageView favoriteIcon, recentlyIcon, suggestedIcon;
SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        mapping();
        String token = getToken();

        Intent intent = getIntent();
        User user = (User) getIntent().getSerializableExtra("user");
        txtusername.setText(user.getUsername());

        tbSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String query = tbSearch.getText().toString().trim();
                    Intent intent = new Intent(Home.this, List_food.class);
                    intent.putExtra("search_query", query);
                    intent.putExtra("user", user);
                    intent.putExtra("list", "search");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        viewCreatefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Add_food.class);
                startActivity(intent);
            }
        });

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "favorite");
                startActivity(intent);

            }
        });

        recentlyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "viewed");
                startActivity(intent);
            }
        });
    }

    private void mapping(){
        txtusername = findViewById(R.id.txtname);
        tbSearch = findViewById(R.id.home_search);
        avatar = findViewById(R.id.avatar);
        viewRandomfood = findViewById(R.id.rcmrandomfood);
        viewCreatefood = findViewById(R.id.createdish);
        favoriteIcon = findViewById(R.id.btnfavorite);
        recentlyIcon = findViewById(R.id.btncooked);
        suggestedIcon = findViewById(R.id.btnsuggested);
    }

    private String getToken(){
        try {
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            shared = EncryptedSharedPreferences.create(
                    "KitchenWhizToken",
                    masterKey,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("SharedPrefs", e.getMessage());
            shared = getSharedPreferences("KitchenWhizToken", MODE_PRIVATE);
        }
        String token = shared.getString("access_token", null);
        if (token == null){
            Toast.makeText(this, "Access Token không tìm thấy", Toast.LENGTH_SHORT).show();
            return null;
        }
        return token;
    }
}