package com.example.cshare.Views.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cshare.Models.Auth.LoginForm;
import com.example.cshare.R;
import com.example.cshare.ViewModels.AuthViewModel;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthViewModel authViewModel;
    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;

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
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        //  Set click listeners
        buttonLogin.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);

        // Instantiate view model
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        authViewModel.getIsLoggedInMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoggedIn) {
                if (isLoggedIn) {
                    // TODO: Only in case of success

                    // Update the viewModels
                    productViewModel.update();
                    profileViewModel.update();
                    // Redirect to the MainActivity
                    Intent toMainActivityIntent = new Intent();
                    toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivityIntent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            // Validate form and send Login request
            case (R.id.buttonLogin):

                // Retrieve information from the UI
                LoginForm loginUser = new LoginForm(emailAddressEditText.getText().toString().trim().toLowerCase(),
                        passwordEditText.getText().toString().trim());

                if (isValid(loginUser)) {
                    authViewModel.logIn(loginUser);
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

    private Boolean isValid(LoginForm loginForm) {

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

