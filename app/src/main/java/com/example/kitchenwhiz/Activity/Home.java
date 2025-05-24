package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import com.bumptech.glide.Glide;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;
import com.google.android.material.imageview.ShapeableImageView;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
TextView txtusername, txtwhattoeat, txthome_food, txtdes;
EditText tbSearch;
ShapeableImageView avatar, image_food;
View viewRandomfood, viewCreatefood;
ImageView favoriteIcon, recentlyIcon, suggestedIcon;
SharedPreferences shared;
GifImageView fact_food;

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

        getTime(txtwhattoeat);
        RandomFood(user);


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

        suggestedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "top");
                startActivity(intent);
            }
        });

        fact_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fact_food.post(() -> {
                    int[] location = new int[2];
                    fact_food.getLocationOnScreen(location);
                    int x = location[0] - fact_food.getWidth() - 730;
                    int y = location[1];

                    showPopup(x, y);
                });
            }
        });
    }

    private void mapping(){
        txtusername = findViewById(R.id.txtname);
        tbSearch = findViewById(R.id.home_search);
        avatar = findViewById(R.id.avatar);
        image_food = findViewById(R.id.home_img_food);
        viewRandomfood = findViewById(R.id.rcmrandomfood);
        viewCreatefood = findViewById(R.id.createdish);
        favoriteIcon = findViewById(R.id.btnfavorite);
        recentlyIcon = findViewById(R.id.btncooked);
        suggestedIcon = findViewById(R.id.btnsuggested);
        txtwhattoeat = findViewById(R.id.txtwhattoeat);
        txthome_food = findViewById(R.id.txthome_food);
        txtdes = findViewById(R.id.txtdescrip_food);
        fact_food = findViewById(R.id.random_fact);
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

    private void getTime(TextView txtwhattoeat) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int totalMinutes = hour * 60 + minute;

        if (inRange(totalMinutes, 4*60, 9*60+59)) {
            txtwhattoeat.setText("Kitchenwhiz lo bữa sáng!");
        }
        else if (inRange(totalMinutes, 10*60, 14*60+59)) {
            txtwhattoeat.setText("Trưa ăn gì? Kitchenhiz lo");
        }
        else if (inRange(totalMinutes, 15*60, 19*60 + 59)){
            txtwhattoeat.setText("Kitchenwhiz – Tối ngon!");
        }
        else {
            txtwhattoeat.setText("Đói khuya? Có Kitchenwhiz");
        }
    }

    private static boolean inRange(int value, int start, int end) {
        return value >= start && value <= end;
    }

    private void RandomFood(User user) {
        RetrofitClient.getApiService().randomRecipe().enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                try {

                    if (response.isSuccessful() && response.body() != null) {
                        RecipeModel recipeModel = response.body();
                        Glide.with(Home.this)
                                .load(recipeModel.getImage())
                                .placeholder(R.drawable.loading_icon)
                                .error(R.drawable.error)
                                .into(image_food);
                        txthome_food.setText(recipeModel.getTitle());
                        txtdes.setText(recipeModel.getSummary());
                        String foodid = recipeModel.getId();
                        viewRandomfood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Home.this, Food_imformation.class);
                                intent.putExtra("Foodid", foodid);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        image_food.setImageResource(R.drawable.error);
                        txthome_food.setText("Hết ý tưởng món rồi!");
                        txtdes.setText("");
                }} catch (Exception e) {
                    Log.d("API_RANDOM", e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {

            }
        });
    }

    private void showPopup(int x, int y) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.NO_GRAVITY, x, y);


        TextView popupText = popupView.findViewById(R.id.popup_text);
    }
}