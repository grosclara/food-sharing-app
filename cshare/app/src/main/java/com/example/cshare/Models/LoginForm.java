package com.example.cshare.Models;

import android.util.Patterns;

/**
 * Login form model
 */
public class LoginForm {

    private String emailAddress;
    private String password;

    public LoginForm(String emailAddress, String password) {
        emailAddress = emailAddress;
        password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
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
