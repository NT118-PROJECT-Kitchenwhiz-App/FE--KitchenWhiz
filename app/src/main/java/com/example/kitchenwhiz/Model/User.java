package com.example.kitchenwhiz.Model;

import java.io.Serializable;

public class User implements Serializable {
    String _id;
    String username;
    String token;
    String email;

    public User(String id, String username, String accesstoken, String email) {
        this._id = id;
        this.email = email;
        this.token = accesstoken;
        this.email = email;
    }

    public String getId() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccesstoken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
