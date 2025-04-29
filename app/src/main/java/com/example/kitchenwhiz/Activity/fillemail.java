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
                    if (tbfillemail.getText().toString().isEmpty()){
                        Toast.makeText(fillemail.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
}