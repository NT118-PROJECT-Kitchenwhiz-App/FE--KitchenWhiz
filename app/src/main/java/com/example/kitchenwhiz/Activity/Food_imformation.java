package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.kitchenwhiz.Model.Ingredients;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.ApiService;
import com.example.kitchenwhiz.Service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Food_imformation extends AppCompatActivity {
    ImageView image_food, image_back, favorite_button;
    TextView titlefood, servings, time, ingredients, step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_imformation);

        mapping();
        Intent intent = getIntent();
        String id = intent.getStringExtra("Foodid");
        getInformation(id);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mapping(){
        image_food = findViewById(R.id.image_food);
        image_back = findViewById(R.id.image_back);
        favorite_button = findViewById(R.id.like_button);
        titlefood = findViewById(R.id.titlefood);
        servings = findViewById(R.id.num_servings);
        time = findViewById(R.id.time);
        ingredients = findViewById(R.id.ingredients);
        step = findViewById(R.id.step);
    }

    private void getInformation(String id){
        ApiService apiService = RetrofitClient.getApiService();
        Call<RecipeModel> call = apiService.getRecipeById(id);
        call.enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    RecipeModel recipe = response.body();
                    titlefood.setText(recipe.getTitle());
                    try {
                        if (!recipe.getImage().isEmpty()) {
                            Glide.with(Food_imformation.this)
                                    .load(recipe.getImage())
                                    .placeholder(R.drawable.loading_icon)
                                    .into(image_food);
                        }
                        else {
                            image_food.setImageResource(R.drawable.error);
                        }
                    } catch (Exception e) {
                        Log.d("IMAGE_ERROR", e.getMessage());
                        throw new RuntimeException(e);
                    }

                    servings.setText(String.valueOf(recipe.getServings()));
                    StringBuilder stringBuilder = new StringBuilder();
                    List<Ingredients> ingredientList = recipe.getIngredients();
                    if (ingredientList != null) {
                        for (Ingredients ingredient : ingredientList) {
                            StringBuilder s = new StringBuilder();
                            s.append(ingredient.getName()).append(": ");
                            s.append(ingredient.getAmount()).append(" ");
                            s.append(ingredient.getUnit()).append("\n");
                            stringBuilder.append(s.toString());
                        }
                        ingredients.setText(stringBuilder.toString());
                    } else {
                        ingredients.setText("Không có nguyên liệu");
                    }

                    step.setText(recipe.getInstructions() != null ? recipe.getInstructions() : "Không có hướng dẫn");



                }
                else {
                    Toast.makeText(Food_imformation.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("API_GETFOODBYID", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {

            }
        });
    }
}