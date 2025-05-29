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

import com.example.kitchenwhiz.Model.ResetPasswordRequest;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reset_password extends AppCompatActivity {
Button btncancel, btnverify;
EditText tbpass, tbcfpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        mapping();
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reset_password.this, Login.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = tbpass.getText().toString();
                if (newPassword.isEmpty()){
                    Toast.makeText(Reset_password.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(tbcfpass.getText().toString())){
                    Toast.makeText(Reset_password.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validatepass(newPassword)){
                    Toast.makeText(Reset_password.this, "Mật khẩu phải có độ dài ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                ResetPasswordRequest request = new ResetPasswordRequest(email, newPassword);
                ResetPass(request);
                btnverify.setEnabled(true);
            }
        });
    }

    private void mapping(){
        btncancel = findViewById(R.id.fp_cancel);
        btnverify = findViewById(R.id.fp_change);
        tbpass = findViewById(R.id.forgotpass_password);
        tbcfpass = findViewById(R.id.forgotps_password);
    }
    private boolean validatepass(String password){
        return (password.length()>=6);
    }
    private void ResetPass(ResetPasswordRequest request){
        btnverify.setEnabled(false);
        RetrofitClient.getUserApiService(this, null).resetPassword(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(Reset_password.this, "Đổi mật khẩu thành công. Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Reset_password.this, Login.class);
                    startActivity(intent);
                }
                else{
                    Log.d("RESETPASS", response.errorBody().toString());
                    Toast.makeText(Reset_password.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Reset_password.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}