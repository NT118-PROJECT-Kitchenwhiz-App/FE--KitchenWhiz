package com.example.kitchenwhiz.Activity;

import android.content.Intent;
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

import com.example.kitchenwhiz.Model.RegisterRequest;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signup extends AppCompatActivity {
EditText tbemail,  tbusername, tbpass, tbcfpass;
Button btnsignup;
TextView txtlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            btnsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = tbemail.getText().toString().trim();
                    String username = tbusername.getText().toString().trim();
                    String password = tbpass.getText().toString();
                    String cfpassword = tbcfpass.getText().toString();
                    if (!validateemail(email)){
                        Toast.makeText(signup.this, "Vui lòng nhập đúng định dạng email", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validateusername(username)){
                        Toast.makeText(signup.this, "Tên tài khoản phải có độ dài từ 6-20 từ và không chứa các kí tự đặc biệt", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validatepass(password)){
                        Toast.makeText(signup.this, "Mật khẩu phải có độ dài ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!password.equals(cfpassword)){
                        Toast.makeText(signup.this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                        return;
                    }

                    RegisterRequest request = new RegisterRequest(email, username, password);
                    RegisterUser(request);
                }
            });
                    txtlogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(signup.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
            return insets;
        });
    }

    private void mapping(){
        tbemail = findViewById(R.id.signupemail);
        tbusername = findViewById(R.id.signupusername);
        tbpass = findViewById(R.id.signuppassword);
        tbcfpass = findViewById(R.id.signupcfpassword);
        btnsignup = findViewById(R.id.signup_button);
        txtlogin = findViewById(R.id.login);
    }

    private boolean validateemail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.]+$";
        return email.matches(regex);
    }

    private boolean validateusername(String username){
        String regex = "^[A-Za-z0-9+_.-]{3,20}$";
        return username.matches(regex);
    }

    private boolean validatepass(String password){
        return (password.length()>=6);
    }

    private void RegisterUser(RegisterRequest request){
        RetrofitClient.getApiService().registerUser(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                int statusCode = response.code();/*
                try{
                    if (response.body() != null) {
                    String responsebody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responsebody);
                    message = jsonObject.optString("success", jsonObject.optString("message", "No message"));
                } else if (response.errorBody() != null) {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        message = jsonObject.optString("message", "Error occurred");
                    }
                }
                catch (Exception e){
                    Log.e("API Register", e.getMessage());
                    message = "Error";
                }*/
                if (response.isSuccessful()) {
                    Toast.makeText(signup.this, "Đăng ký thành công. Vui lòng kiểm tra mail (bao gồm mail rác) để xác nhận OTP", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(signup.this, verify_otp.class);
                    intent.putExtra("email", request.getEmail());
                    intent.putExtra("status", 1);
                    startActivity(intent);
                }
                else{
                    switch (statusCode) {
                        case 400:
                            message = "Bad request: " + message;
                            break;
                        case 409:
                            message = "Email hoặc tên tài khoản đã tồn tại";
                            break;
                        default:
                            message = "Lỗi " + statusCode + ": " + message;
                            break;
                    }
                    Toast.makeText(signup.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API Register", "Failed: " + t.getMessage());
                Toast.makeText(signup.this, "Không thể kết nối đến server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}