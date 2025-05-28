package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.TTSResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TTSApiService {
    @Headers("Content-Type: text/plain")
    @POST("tts/v5")
    Call<TTSResponse> convertTextToSpeech(
            @Body String text,
            @Header("api_key") String apiKey
    );
}
