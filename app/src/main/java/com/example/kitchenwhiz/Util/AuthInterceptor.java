package com.example.kitchenwhiz.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.kitchenwhiz.Model.AccessTokenResponse;
import com.example.kitchenwhiz.Model.RefreshTokenRequest;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.Service.RetrofitClient;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

    public class AuthInterceptor implements Interceptor {
        private JWT jwtUtil;
        private User user;
        private Context context;
        private static final String[] PUBLIC_ENDPOINTS = {
                "user/login",
                "user/registration",
                "user/forgotPassword",
                "user/resetPassword",
                "user/verifyOtp"
        };

        public AuthInterceptor(Context context, User user) {
            this.context = context;
            this.user = user;
            this.jwtUtil = new JWT(context);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            String requestUrl = originalRequest.url().encodedPath();

            boolean isPublicEndpoint = false;
            for (String endpoint : PUBLIC_ENDPOINTS) {
                if (requestUrl.endsWith(endpoint)) {
                    isPublicEndpoint = true;
                    break;
                }
            }

            if (isPublicEndpoint || user == null) {
                return chain.proceed(originalRequest);
            }

            String accessToken = jwtUtil.getToken();
            if (accessToken == null) {
                return chain.proceed(originalRequest);
            }

            if (jwtUtil.isTokenExpired(jwtUtil.getToken())) {
                synchronized (this) {
                    String newAccessToken = refreshTokenSync();
                    if (newAccessToken == null) {
                        jwtUtil.clearToken();
                        return chain.proceed(originalRequest);
                    }
                    accessToken = newAccessToken;
                }
            }

            Request modifiedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            Response response = chain.proceed(modifiedRequest);

            if (response.code() == 401) {
                synchronized (this) {
                    String newAccessToken = refreshTokenSync();
                    if (newAccessToken == null) {
                        jwtUtil.clearToken();
                        return response;
                    }
                    accessToken = newAccessToken;
                    Request retryRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + accessToken)
                            .build();
                    return chain.proceed(retryRequest);
                }
            }

            return response;
        }
    private String refreshTokenSync() {
        String refreshToken = user.getRefreshToken();
        if (refreshToken == null) {
            Log.d("JWT_REFRESHTOKEN", "No refresh token available");
            return null;
        }

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        Call<AccessTokenResponse> call = RetrofitClient.getUserApiService(context, user).refreshAccessToken(request);

        try {
            retrofit2.Response<AccessTokenResponse> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                AccessTokenResponse tokenResponse = response.body();
                String newAccessToken = tokenResponse.getAccessToken();
                String expiresAt = tokenResponse.getAccessTokenExpire();

                jwtUtil.saveToken(newAccessToken, expiresAt);
                if (user != null) {
                    user.setAccessToken(newAccessToken);
                }

                Log.d("JWT_REFRESHTOKEN", "success");
                return newAccessToken;
            } else {
                Log.e("JWT_REFRESHTOKEN", response.message());
                if (response.errorBody() != null) {
                    Log.e("JWT_REFRESHTOKEN", response.errorBody().string());
                }
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
