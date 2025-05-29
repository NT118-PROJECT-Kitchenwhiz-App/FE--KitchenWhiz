package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.RecipeModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApiService {
    @Multipart
    @POST("recipe/addRecipe")
    Call<ResponseBody> addRecipe(
            @Part MultipartBody.Part image,
            @Part("recipeInfo") RequestBody recipeInfo
    );
    @GET("recipe/searchByIngredient")
    Call<List<RecipeModel>> searchByIngredient(@Query("name") String name);

    @GET("recipe/{id}")
    Call<RecipeModel> getRecipeById(@Path("id") String id);
    @GET("recipe/likeRecipes")
    Call<List<RecipeModel>> likeRecipes();

    @GET("recipe/randomRecipe")
    Call<RecipeModel> randomRecipe();

    @GET("recipe/searchByRecipe")
    Call<List<RecipeModel>> searchByRecipe(@Query("title") String title);
}
