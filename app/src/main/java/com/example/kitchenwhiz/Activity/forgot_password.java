package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.R;

public class forgot_password extends AppCompatActivity {
Button btncancel, btnverify;
EditText tbpass, tbcfpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(forgot_password.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            btnverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tbpass.getText().toString().isEmpty()){
                        Toast.makeText(forgot_password.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!tbpass.getText().toString().equals(tbcfpass.getText().toString())){
                        Toast.makeText(forgot_password.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            return insets;
        });
    }

    private void mapping(){
        btncancel = findViewById(R.id.fp_cancel);
        btnverify = findViewById(R.id.fp_change);
        tbpass = findViewById(R.id.forgotpass_password);
        tbcfpass = findViewById(R.id.forgotps_password);
    }
}