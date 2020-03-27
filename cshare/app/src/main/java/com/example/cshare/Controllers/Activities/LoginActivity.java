package com.example.cshare.Controllers.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.R;
import com.example.cshare.ViewModels.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private LoginViewModel loginViewModel;

    private EditText emailAddressEditText;
    private EditText passwordEditText;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getUser().observe(this, new Observer<LoginForm>() {
            @Override
            public void onChanged(@Nullable LoginForm loginUser) {

                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getEmailAddress())) {
                    emailAddressEditText.setError("Enter an E-Mail Address");
                    emailAddressEditText.requestFocus();
                } else if (!loginUser.isEmailValid()) {
                    emailAddressEditText.setError("Enter a Valid E-mail Address");
                    emailAddressEditText.requestFocus();
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
                    passwordEditText.setError("Enter a Password");
                    passwordEditText.requestFocus();
                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
                    passwordEditText.setError("Enter at least 6 Digit password");
                    passwordEditText.requestFocus();
                } else {
                    // if valid form
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) { loginViewModel.submitValidForm(); }
    }
}
