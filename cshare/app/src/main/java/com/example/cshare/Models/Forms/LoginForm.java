package com.example.cshare.Models.Forms;

import android.util.Patterns;

/**
 * Login form model
 */
public class LoginForm {

    private String email;
    private String password;

    public LoginForm(String emailAddress, String password) {
        this.email = emailAddress;
        this.password = password;
    }

    public String getEmailAddress() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
