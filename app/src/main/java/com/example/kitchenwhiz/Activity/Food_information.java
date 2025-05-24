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
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.kitchenwhiz.Adapter.Dish_Adapter;
import com.example.kitchenwhiz.Model.Ingredients;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.Model.UserFavoriteRequest;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Food_information extends AppCompatActivity {
    ImageView image_food, image_back, favorite_button;
    TextView titlefood, servings, time, ingredients, step;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_information);

        mapping();

        Intent intent = getIntent();
        String id = intent.getStringExtra("Foodid");
        User user = (User) getIntent().getSerializableExtra("user");

        getInformation(id);
        checkFavoriteFood(user.getId(), id);

        UserFavoriteRequest userViewedRequest = new UserFavoriteRequest(user.getId(), id);
        addViewedFavoriteFood(userViewedRequest);
        setSupportActionBar(toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isVisible = true;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (isVisible) {
                        favorite_button.setVisibility(View.GONE);
                        isVisible = false;
                    }
                } else {
                    if (!isVisible) {
                        favorite_button.setVisibility(View.VISIBLE);
                        isVisible = true;
                    }
                }
            }
        });


        image_back.setOnClickListener(v -> onBackPressed());

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFavoriteRequest userFavoriteRequest = new UserFavoriteRequest(user.getId(), id);
                int currentImageId = (favorite_button.getTag() != null)
                        ? (int) favorite_button.getTag()
                        : R.drawable.heart_icon_2048x1782_hc4h9q6s;

                if (currentImageId == R.drawable.heart_icon_2048x1782_hc4h9q6s) {
                    addUserFavoriteFood(userFavoriteRequest);
                    favorite_button.setImageResource(R.drawable.full_heart_icon);
                    favorite_button.setTag(R.drawable.full_heart_icon);
                } else {
                    deleteFavoriteFood(userFavoriteRequest);
                    favorite_button.setImageResource(R.drawable.heart_icon_2048x1782_hc4h9q6s);
                    favorite_button.setTag(R.drawable.heart_icon_2048x1782_hc4h9q6s);
                }
            }
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
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
    }

    private void getInformation(String id){
        RetrofitClient.getRecipeApiService().getRecipeById(id).enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                if (response.isSuccessful()) {
                    RecipeModel recipe = response.body();
                    titlefood.setText(recipe.getTitle());
                    collapsingToolbarLayout.setTitle(recipe.getTitle());
                    try {
                        if (!recipe.getImage().isEmpty()) {
                            Glide.with(Food_information.this)
                                    .load(recipe.getImage())
                                    .placeholder(R.drawable.loading_icon)
                                    .error(R.drawable.error)
                                    .into(image_food);
                        }
                        else {
                            image_food.setImageResource(R.drawable.error);
                        }
                    } catch (Exception e) {
                        Log.d("IMAGE_ERROR", e.getMessage());
                        throw new RuntimeException(e);
                    }

                    time.setText("Thời gian chuẩn bị " + String.valueOf(recipe.getReadyInMinutes()) + " phút");

                    servings.setText(String.valueOf(recipe.getServings()));
                    StringBuilder stringBuilder = new StringBuilder();
                    List<Ingredients> ingredientList = recipe.getIngredients();
                    if (ingredientList != null) {
                        for (Ingredients ingredient : ingredientList) {
                            StringBuilder s = new StringBuilder();
                            s.append(ingredient.getName());
                            double amount = ingredient.getAmount();
                            if (amount > 0) {
                                s.append(": ");
                                if (amount == (int) amount) {
                                    s.append((int) amount + " ");
                                } else {
                                    s.append(amount + " ");
                                }
                            }
                            if (ingredient.getUnit() != null)
                                s.append(ingredient.getUnit());
                            s.append('\n');
                            stringBuilder.append(s.toString());
                        }
                        ingredients.setText(stringBuilder.toString());
                    } else {
                        ingredients.setText("Không có nguyên liệu");
                    }

                    step.setText(recipe.getInstructions() != null ? recipe.getInstructions() : "Không có hướng dẫn");



                }
                else {
                    Toast.makeText(Food_information.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("API_GETFOODBYID", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Log.d("FAIL_API_GET_FOOD", t.getMessage());

            }
        });
    }
    
    private void addUserFavoriteFood(UserFavoriteRequest userFavoriteRequest) {
        RetrofitClient.getUserApiService().addFavoriteRecipes(userFavoriteRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Food_information.this, "Đã thêm món ăn vào yêu thích", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Food_information.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("API_FAVORITE", response.message());
                }
                
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    
    private void addViewedFavoriteFood(UserFavoriteRequest userViewedRequest){
        RetrofitClient.getUserApiService().addViewedRecipes(userViewedRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("API_VIEWED", response.code() + " " + response.body().toString());
                if (!response.isSuccessful()) {
                    Toast.makeText(Food_information.this, "Không thể thêm món ăn vào danh sách đã xem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void checkFavoriteFood(String userid, String foodid) {
        RetrofitClient.getUserApiService().allFavoriteRecipes(userid).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                favorite_button.setTag(R.drawable.heart_icon_2048x1782_hc4h9q6s);
                if (response.isSuccessful()){
                    List<RecipeModel> recipeModel = response.body();
                    for (RecipeModel model : recipeModel) {
                        if(model.getId().equals(foodid)) {
                            favorite_button.setImageResource(R.drawable.full_heart_icon);
                            favorite_button.setTag(R.drawable.full_heart_icon);
                            break;
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }
        });
    }

    private void deleteFavoriteFood(UserFavoriteRequest userFavoriteRequest) {
        RetrofitClient.getUserApiService().deleteFavoriteRecipe(userFavoriteRequest.getUser_id(), userFavoriteRequest.getRecipe_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Food_information.this, "Đã xóa món ăn khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Food_information.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}