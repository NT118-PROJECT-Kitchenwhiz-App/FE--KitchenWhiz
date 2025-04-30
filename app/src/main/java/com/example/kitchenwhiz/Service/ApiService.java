package com.example.kitchenwhiz.Service;

import com.example.kitchenwhiz.Model.ForgotPassRequest;
import com.example.kitchenwhiz.Model.OTPRequest;
import com.example.kitchenwhiz.Model.RegisterRequest;
import com.google.gson.annotations.SerializedName;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/registration")
    Call<ResponseBody> registerUser(@Body RegisterRequest request);

    @POST("user/vertifyOtp")
    Call<ResponseBody> verifyOTP(@Body OTPRequest request);

    @POST("user/forgotPassword")
    Call<ResponseBody> forgotPassword(@Body ForgotPassRequest request);

}
