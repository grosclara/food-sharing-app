package com.example.cshare.Models.Auth;

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

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmailAddress()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }
}
