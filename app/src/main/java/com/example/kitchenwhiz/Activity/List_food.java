package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.Adapter.Dish_Adapter;
import com.example.kitchenwhiz.Model.RecipeInfo;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.ApiService;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_food);
        mapping();

        List<RecipeModel> arrDish = new ArrayList<>();
        Dish_Adapter dishAdapter = new Dish_Adapter(this, R.layout.dish_item, arrDish);
        listFood.setAdapter(dishAdapter);
        Intent intent = getIntent();
        User user = (User) getIntent().getSerializableExtra("user");
        String home_query = intent.getStringExtra("search_query");
        if (!home_query.isEmpty()) {
            txt_Search.setText(home_query);
        }

        searchRecipe(home_query, arrDish, dishAdapter);

        txt_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    String query = txt_Search.getText().toString().trim().toLowerCase();
                    searchRecipe(query, arrDish, dishAdapter);

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txt_Search.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        listFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeModel recipeModel = arrDish.get(position);
                Intent intent = new Intent(List_food.this, Food_imformation.class);
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
    }

    private void searchRecipe(String name, List<RecipeModel> arr, Dish_Adapter dishAdapter){
        RetrofitClient.getApiService().searchByIngredient(name).enqueue(new Callback<List<RecipeModel>>() {
            @Override
            public void onResponse(Call<List<RecipeModel>> call, Response<List<RecipeModel>> response) {
                if (response.isSuccessful()){
                    arr.clear();
                    arr.addAll(response.body());
                    dishAdapter.notifyDataSetChanged();
                    if (arr.isEmpty()) {
                        listFood.setVisibility(View.GONE);
                        noResultsLayout.setVisibility(View.VISIBLE);
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
                }
                else {
                    Toast.makeText(List_food.this, response.message(), Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeModel>> call, Throwable t) {

            }

    });
    }
}