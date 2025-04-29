package com.example.kitchenwhiz.Model;

public class OTPRequest {

    private String email;
    private String otpCode;

    public OTPRequest(String email, String otpCode){
        this.email = email;
        this.otpCode = otpCode;
    }
    public String getEmail() {
        return email;
    }

    public String getOTP() {
        return otpCode;
    }

    public void setOTP(String OTP) {
        this.otpCode = OTP;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
