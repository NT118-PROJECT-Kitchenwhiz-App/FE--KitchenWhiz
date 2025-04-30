package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.Model.ForgotPassRequest;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fillemail extends AppCompatActivity {
Button btncancel, btnverify;
EditText tbfillemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fillemail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(fillemail.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            btnverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = tbfillemail.getText().toString().trim();
                    if (email == null){
                        Toast.makeText(fillemail.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ForgotPassRequest request = new ForgotPassRequest(email);
                    ForgotPass(request);
                }
            });
            return insets;
        });
    }

    private void mapping(){
        btncancel = findViewById(R.id.forgotpass_cancel);
        btnverify = findViewById(R.id.forgotpassverify_button);
        tbfillemail = findViewById(R.id.forgotpass_email);
    }

    private void ForgotPass(ForgotPassRequest request){
        RetrofitClient.getApiService().forgotPassword(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(fillemail.this, "Vui lòng xác nhận OTP", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(fillemail.this, verify_otp.class);
                    intent.putExtra("email", request.getEmail());
                    intent.putExtra("status", 2);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(fillemail.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API FORGOTPASS", "Failed: " + t.getMessage());
                Toast.makeText(fillemail.this, "Không thể kết nối đến server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}