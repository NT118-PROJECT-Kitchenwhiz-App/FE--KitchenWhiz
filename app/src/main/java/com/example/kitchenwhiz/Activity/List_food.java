package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kitchenwhiz.Adapter.Dish_Adapter;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class List_food extends AppCompatActivity {
ListView listFood;
EditText txt_Search;
View noResultsLayout;
TextView setnofound;
ImageButton image_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);
        mapping();


        Intent intent = getIntent();
        User user = (User) getIntent().getSerializableExtra("user");
        String list = intent.getStringExtra("list");
        Log.d("CHECK", list);

        List<RecipeModel> arrDish = new ArrayList<>();
        Dish_Adapter dishAdapter = new Dish_Adapter(this, R.layout.dish_item, arrDish, list);
        listFood.setAdapter(dishAdapter);

        List<RecipeModel> listsearch = new ArrayList<>();
        if (list.equals("search")) {

            String home_query = intent.getStringExtra("search_query");
            if (!home_query.isEmpty()) {
                txt_Search.setText(home_query);
            }

            getRecipebyname(home_query, dishAdapter, arrDish, user);
        }
        else if (list.equals("favorite")) {
            getallFavoriteFoods(user, arrDish, dishAdapter);

        }
        else if (list.equals("viewed")){
            getViewedFood(user, dishAdapter, arrDish);
        }
        else if (list.equals("top")) {
            getTopFoods(dishAdapter, arrDish, user);
        }

        txt_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    String query = txt_Search.getText().toString().trim().toLowerCase();

                    getRecipebyname(query, dishAdapter, arrDish, user);

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txt_Search.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = txt_Search.getText().toString().trim().toLowerCase();

                getRecipebyname(query, dishAdapter, arrDish, user);

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txt_Search.getWindowToken(), 0);
            }
        });

        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeModel recipeModel = arrDish.get(position);
                Intent intent = new Intent(List_food.this, Food_information.class);
                intent.putExtra("Foodid", recipeModel.getId());
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        listFood = findViewById(R.id.listFood);
        txt_Search = findViewById(R.id.home_search);
        noResultsLayout = findViewById(R.id.no_results_layout);
        setnofound = findViewById(R.id.explains);
        image_search = findViewById(R.id.image_search);
    }

    private void searchRecipe(String name, List<RecipeModel> arr, Dish_Adapter dishAdapter, User user){
        RetrofitClient.getRecipeApiService(this, user).searchByIngredient(name).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()){
                    for (RecipeModel recipe : response.body()) {
                        if (!containsRecipe(arr, recipe)) {
                            arr.add(recipe);
                        }
                    }
                    dishAdapter.notifyDataSetChanged();
                    if (arr.isEmpty()) {
                        listFood.setVisibility(View.GONE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        setnofound.setText("Hãy thử kiếm tra chính tả hoặc\ncó vẻ món ăn đó chúng tôi chưa biết tới");
                    }
                    else {
                        listFood.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                    }
                }
                else {
                    arr.clear();
                    dishAdapter.updateRecipes(arr);
                    listFood.setVisibility(View.GONE);
                    noResultsLayout.setVisibility(View.VISIBLE);
                    setnofound.setText("Hãy thử kiếm tra chính tả hoặc\ncó vẻ món ăn đó chúng tôi chưa biết tới");
                    Toast.makeText(List_food.this, response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(List_food.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }

    });
    }

    private void getallFavoriteFoods(User user, List<RecipeModel> arr, Dish_Adapter dishAdapter) {
        RetrofitClient.getUserApiService(this, user).allFavoriteRecipes().enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                Log.d("CHECK_DATA", response.message());
                if (response.isSuccessful()){
                    arr.clear();
                    arr.addAll(response.body());
                    dishAdapter.notifyDataSetChanged();
                    if (arr.isEmpty()) {
                        listFood.setVisibility(View.GONE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        setnofound.setText("Có vẻ bạn chưa\nyêu thích món ăn nào");
                    } else {
                        listFood.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                    }
                }
                else if (response.code() == 404) {
                    arr.clear();
                    dishAdapter.updateRecipes(arr);
                    listFood.setVisibility(View.GONE);
                    noResultsLayout.setVisibility(View.VISIBLE);
                    setnofound.setText("Có vẻ bạn chưa\nyêu thích món ăn nào");
                }
                else {
                    Toast.makeText(List_food.this, response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(List_food.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getViewedFood(User user, Dish_Adapter dishAdapter, List<RecipeModel> arr) {
        RetrofitClient.getUserApiService(this, user).allViewRecipes().enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()){
                    arr.clear();
                    arr.addAll(response.body());
                    dishAdapter.notifyDataSetChanged();
                    if (arr.isEmpty()) {
                        listFood.setVisibility(View.GONE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        setnofound.setText("Có vẻ như bạn chưa từng\nxem món ăn nào");
                    } else {
                        listFood.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                    }
                }
                else if (response.code() == 404) {
                    arr.clear();
                    dishAdapter.updateRecipes(arr);
                    listFood.setVisibility(View.GONE);
                    noResultsLayout.setVisibility(View.VISIBLE);
                    setnofound.setText("Có vẻ như bạn chưa từng\nxem món ăn nào");
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(List_food.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTopFoods(Dish_Adapter dishAdapter, List<RecipeModel> arr, User user) {
        RetrofitClient.getRecipeApiService(this, user).likeRecipes().enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()){
                    arr.clear();
                    arr.addAll(response.body());
                    dishAdapter.notifyDataSetChanged();
                    if (arr.isEmpty()) {
                        listFood.setVisibility(View.GONE);
                        noResultsLayout.setVisibility(View.VISIBLE);
                        setnofound.setText("Hiện tại chúng tôi chưa biết\nnên gợi ý bạn món nào");
                    } else {
                        listFood.setVisibility(View.VISIBLE);
                        noResultsLayout.setVisibility(View.GONE);
                    }
                }
                else if (response.code() == 404) {
                    arr.clear();
                    dishAdapter.updateRecipes(arr);
                    listFood.setVisibility(View.GONE);
                    noResultsLayout.setVisibility(View.VISIBLE);
                    setnofound.setText("Hiện tại chúng tôi chưa biết\nnên gợi ý bạn món nào");
                }
                else {
                    Toast.makeText(List_food.this, response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(List_food.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRecipebyname(String name, Dish_Adapter dishAdapter, List<RecipeModel> arr, User user) {
        RetrofitClient.getRecipeApiService(this, user).searchByRecipe(name).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
               Log.d("API_SEARCHBYNAME", name);
                if (response.isSuccessful()) {
                    arr.clear();
                    arr.addAll(response.body());
                    dishAdapter.notifyDataSetChanged();

                    searchRecipe(name, arr, dishAdapter, user);

                }
                else {
                    Toast.makeText(List_food.this, response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {
                Toast.makeText(List_food.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean containsRecipe(List<RecipeModel> list, RecipeModel item) {
        for (RecipeModel model : list) {
            if (model.getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }


}