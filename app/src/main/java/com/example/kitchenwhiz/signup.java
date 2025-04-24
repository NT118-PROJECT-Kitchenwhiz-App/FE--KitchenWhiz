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

import java.util.regex.Pattern;

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
                    if (!validateemail(tbemail.getText().toString().trim())){
                        Toast.makeText(signup.this, "Vui lòng nhập đúng định dạng email", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validateusername(tbusername.getText().toString().trim())){
                        Toast.makeText(signup.this, "Tên tài khoản phải có độ dài từ 6-20 từ và không chứa các kí tự đặc biệt", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!validatepass(tbpass.getText().toString())){
                        Toast.makeText(signup.this, "Mật khẩu phải có độ dài ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!tbpass.getText().toString().equals(tbcfpass.getText().toString())){
                        Toast.makeText(signup.this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
                        return;
                    }
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
}