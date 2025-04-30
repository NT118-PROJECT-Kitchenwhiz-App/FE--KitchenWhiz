package com.example.kitchenwhiz.Model;

public class ForgotPassRequest {
    private String email;
    public ForgotPassRequest(String email){
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
