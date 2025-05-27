package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.RandomFactResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FactApiService {
    @GET("fact/getRandomFact")
    Call<RandomFactResponse> getRandomFact();
}
