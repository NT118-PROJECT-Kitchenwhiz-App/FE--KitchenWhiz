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

import com.example.kitchenwhiz.Model.OTPRequest;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class verify_otp extends AppCompatActivity {
    EditText tbotp;
    Button btncancel, btnverify;
    TextView txtgetcode;
    Integer status = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            Intent intent = getIntent();
            String email = intent.getStringExtra("email");
            status = intent.getIntExtra("status", 0);
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(verify_otp.this, signup.class);
                    startActivity(intent);
                }
            });

            btnverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String otpCode = tbotp.getText().toString().trim();
                    if (otpCode == null){
                        Toast.makeText(verify_otp.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    OTPRequest request = new OTPRequest(email, otpCode);
                    OTP(request);
                }
            });
            return insets;
        });
    }

    private void mapping(){
        tbotp = findViewById(R.id.otp);
        btncancel = findViewById(R.id.fp_cancel);
        btnverify = findViewById(R.id.fp_change);
        txtgetcode = findViewById(R.id.getotpagain);
    }

    private void OTP(OTPRequest request){
        RetrofitClient.getApiService().verifyOTP(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                int Statuscode = response.code();
                /*try{
                    if (response.body() != null) {
                        String responsebody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responsebody);
                        message = jsonObject.optString("message", "No message");
                    } else if (response.errorBody() != null) {
                        String responsebody = response.errorBody().string();
                        //JSONObject jsonObject = new JSONObject(responsebody);
                        //message = jsonObject.optString("message", "No message");

                    }
                }
                catch (Exception e){
                    Log.e("API OTP", e.getMessage());
                    message = "Error";
                }*/
                if (response.isSuccessful()){
                    if (status == 1){
                    Toast.makeText(verify_otp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(verify_otp.this, MainActivity.class);
                    startActivity(intent);
                    }
                    else {
                        Toast.makeText(verify_otp.this, "Quên mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(verify_otp.this, forgot_password.class);
                        startActivity(intent);
                    }
                }
                else{
                    Log.d("OTP", request.getOTP() + " " + status + " " + request.getEmail());
                    Toast.makeText(verify_otp.this, "Sai mã OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API OTP", "Failed: " + t.getMessage());
                Toast.makeText(verify_otp.this, "Không thể kết nối đến server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}