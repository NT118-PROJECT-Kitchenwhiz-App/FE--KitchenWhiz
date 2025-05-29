package com.example.kitchenwhiz.Service;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.Util.AuthInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://be-kitchenwhiz.onrender.com/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance(Context context, User user) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context, user))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static UserApiService getUserApiService(Context context, User user) {
        return getRetrofitInstance(context, user).create(UserApiService.class);
    }

    public static RecipeApiService getRecipeApiService(Context context, User user) {
        return getRetrofitInstance(context, user).create(RecipeApiService.class);
    }

    public static FactApiService getFactApiService(Context context, User user) {
        return getRetrofitInstance(context, user).create(FactApiService.class);
    }
}