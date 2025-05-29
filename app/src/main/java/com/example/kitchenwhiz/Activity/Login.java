package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.kitchenwhiz.Model.LoginRequest;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;
import com.example.kitchenwhiz.Util.JWT;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
Button btnlogin;
EditText tbname, tbpass;
TextView txtforgotpass, txtregister;
SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

            mapping();
            btnlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = tbname.getText().toString().trim();
                    String pass = tbpass.getText().toString();
                    if (username.isEmpty() || pass.isEmpty()){
                        Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LoginRequest request = new LoginRequest(username, pass);
                    Login(request);
                }
            });

            txtforgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Forgot_password.class);
                    startActivity(intent);
                }
            });

            txtregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Signup.class);
                    startActivity(intent);
                }
            });
    }

    private void mapping(){
        btnlogin = findViewById(R.id.login_button);
        tbname = findViewById(R.id.email_username);
        tbpass = findViewById(R.id.loginpassword);
        txtforgotpass = findViewById(R.id.forgot_password);
        txtregister = findViewById(R.id.sign_up);
    }

    private void Login(LoginRequest request) {
        RetrofitClient.getUserApiService(this, null).login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (response.isSuccessful()) {
                    if (!user.getAccesstoken().isEmpty()) {
                        JWT jwt = new JWT(Login.this);
                        jwt.saveToken(user.getAccesstoken(), user.getAccessTokenExpire());
                    }
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Tên tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    Log.d("FAILED LOGIN", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(Login.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}