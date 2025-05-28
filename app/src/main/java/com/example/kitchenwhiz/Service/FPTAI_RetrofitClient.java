package com.example.kitchenwhiz.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FPTAI_RetrofitClient {
    private static final String BASE_URL = "https://api.fpt.ai/hmi/";
    private static Retrofit retrofit = null;

    public static TTSApiService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TTSApiService.class);
    }
}
