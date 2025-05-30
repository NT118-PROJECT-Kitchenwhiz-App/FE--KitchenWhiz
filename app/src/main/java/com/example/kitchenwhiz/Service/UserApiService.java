package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.AccessTokenResponse;
import com.example.kitchenwhiz.Model.ForgotPassRequest;
import com.example.kitchenwhiz.Model.LoginRequest;
import com.example.kitchenwhiz.Model.Logoutrequest;
import com.example.kitchenwhiz.Model.OTPRequest;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.RefreshTokenRequest;
import com.example.kitchenwhiz.Model.RegisterRequest;
import com.example.kitchenwhiz.Model.ResetPasswordRequest;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.Model.UserFavoriteRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("user/registration")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("user/verifyOtp")
    Call<ResponseBody> verifyOTP(@Body OTPRequest request);

    @POST("user/forgotPassword")
    Call<ResponseBody> forgotPassword(@Body ForgotPassRequest request);

    @POST("user/resetPassword")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest request);

    @POST("user/login")
    Call<User> login(@Body LoginRequest request);

    @POST("user/addFavoriteRecipes")
    Call<ResponseBody> addFavoriteRecipes(@Body UserFavoriteRequest userFavoriteRequest);

    @GET("user/allFavoriteRecipes")
    Call<List<RecipeModel>> allFavoriteRecipes();

    @DELETE("user/favoriteRecipe/{recipe_id}")
    Call<ResponseBody> DeletefavoriteRecipe(@Path("recipe_id") String recipe_id);

    @POST("user/addViewedRecipes")
    Call<ResponseBody> addViewedRecipes(@Body UserFavoriteRequest userviewedRequest);

    @GET("user/allViewRecipes")
    Call<List<RecipeModel>> allViewRecipes();

    @DELETE("user/favoriteRecipe/{recipe_id}")
    Call<ResponseBody> deleteFavoriteRecipe(@Path("recipe_id") String recipe_id);
    @POST("user/logout")
    Call<ResponseBody> logout(@Body Logoutrequest logoutrequest);
    @Multipart
    @POST("user/updateAvatar")
    Call<ResponseBody> updateAvater(@Part MultipartBody.Part image);

    @POST("user/refreshAccessToken")
    Call<AccessTokenResponse> refreshAccessToken(@Body RefreshTokenRequest request);
}
