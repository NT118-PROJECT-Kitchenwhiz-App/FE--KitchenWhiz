package com.example.kitchenwhiz.Model;

public class LoginRequest {
    private String login;
    private String password;
    public LoginRequest(String login, String password){
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
}
