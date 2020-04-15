package com.example.cshare.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.R;
import com.example.cshare.ViewModels.AuthViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private AuthViewModel authViewModel;

    // Views
    private EditText emailAddressEditText;
    private EditText passwordEditText;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind views
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount =findViewById(R.id.buttonCreateAccount);

        //  Set click listeners
        buttonLogin.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);

        // Instantiate view model
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {

            // Validate form and send Login request
            case(R.id.buttonLogin):

                // Retrieve information from the UI
                LoginForm loginUser = new LoginForm(emailAddressEditText.getText().toString().trim().toLowerCase(),
                        passwordEditText.getText().toString().trim());

                if (isValid(loginUser)) {
                    Boolean logInSuccess = authViewModel.logIn(loginUser);

                    if (logInSuccess){

                        Intent toMainActivityIntent = new Intent();
                        toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(toMainActivityIntent);
                    }
                }
                break;

            // Redirect to the Register Activity
            case (R.id.buttonCreateAccount):
                Intent toRegisterActivityIntent = new Intent();
                toRegisterActivityIntent.setClass(getApplicationContext(), RegisterActivity.class);
                startActivity(toRegisterActivityIntent);
                break;

        }
    }

    private Boolean isValid(LoginForm loginForm){

        if (!loginForm.isEmailValid()) {
            emailAddressEditText.setError("Enter a Valid E-mail Address");
            emailAddressEditText.requestFocus();
            return false;
        }

        if (!loginForm.isPasswordLengthGreaterThan5()) {
            passwordEditText.setError("Enter at least 6 Digit password");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

}

