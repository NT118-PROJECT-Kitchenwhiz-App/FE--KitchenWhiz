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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Verify_OTP extends AppCompatActivity {
    EditText tbotp;
    Button btncancel, btnverify;
    TextView txtgetcode;
    Integer status = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        mapping();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        status = intent.getIntExtra("status", 0);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verify_OTP.this, Signup.class);
                startActivity(intent);
            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = tbotp.getText().toString().trim();
                if (otpCode == null){
                    Toast.makeText(Verify_OTP.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                OTPRequest request = new OTPRequest(email, otpCode);
                OTP(request);
            }
        });
    }

    private void mapping(){
        tbotp = findViewById(R.id.otp);
        btncancel = findViewById(R.id.fp_cancel);
        btnverify = findViewById(R.id.fp_change);
        txtgetcode = findViewById(R.id.getotpagain);
    }

    private void OTP(OTPRequest request){
        RetrofitClient.getUserApiService(this, null).verifyOTP(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String message = "";
                int Statuscode = response.code();
                if (response.isSuccessful()){
                    if (status == 1){
                    Toast.makeText(Verify_OTP.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Verify_OTP.this, Login.class);
                    startActivity(intent);
                    }
                    else {
                        Toast.makeText(Verify_OTP.this, "Quên mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Verify_OTP.this, Reset_password.class);
                        intent.putExtra("email", request.getEmail());
                        startActivity(intent);
                    }
                }
                else{
                    Log.d("OTP", request.getOTP() + " " + status + " " + request.getEmail());
                    Toast.makeText(Verify_OTP.this, "Sai mã OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Verify_OTP.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}