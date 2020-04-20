package com.example.cshare.Models.Auth;

import com.example.cshare.Models.User;

public class LoginResponse {

    private String key;
    private User user;

    public LoginResponse(String key, User user) {
        this.key = key;
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public User getUser() {
        return user;
    }
}
