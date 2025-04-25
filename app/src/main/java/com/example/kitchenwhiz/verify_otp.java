package com.example.kitchenwhiz;

import android.content.Intent;
import android.os.Bundle;
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

public class verify_otp extends AppCompatActivity {
    EditText tbotp;
    Button btncancel, btnverify;
    TextView txtgetcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            mapping();
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
                    if (tbotp.getText().toString().isEmpty()){
                        Toast.makeText(verify_otp.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
            return insets;
        });
    }

    private void mapping(){
        tbotp = findViewById(R.id.otp);
        btncancel = findViewById(R.id.otpcancel_button);
        btnverify = findViewById(R.id.verify_button);
        txtgetcode = findViewById(R.id.getotpagain);
    }
}