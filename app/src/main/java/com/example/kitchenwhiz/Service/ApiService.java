package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.ForgotPassRequest;
import com.example.kitchenwhiz.Model.LoginRequest;
import com.example.kitchenwhiz.Model.OTPRequest;
import com.example.kitchenwhiz.Model.RecipeInfo;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.RegisterRequest;
import com.example.kitchenwhiz.Model.ResetPasswordRequest;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @POST("user/registration")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("user/vertifyOtp")
    Call<ResponseBody> verifyOTP(@Body OTPRequest request);

    @POST("user/forgotPassword")
    Call<ResponseBody> forgotPassword(@Body ForgotPassRequest request);

    @POST("user/resetPassword")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest request);

    @POST("user/login")
    Call<ResponseBody> login(@Body LoginRequest request);
    @Multipart
    @POST("recipe/addRecipe")
    Call<ResponseBody> addRecipe(
            @Part MultipartBody.Part image,
            @Part("recipeInfo") RequestBody recipeInfo
    );
    @GET("recipe/searchByIngredient")
    Call<List<RecipeModel>> searchByIngredient(@Query("name") String name);

}
