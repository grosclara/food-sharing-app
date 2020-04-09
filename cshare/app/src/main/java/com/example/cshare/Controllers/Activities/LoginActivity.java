package com.example.cshare.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.LoginResponse;
import com.example.cshare.R;
import com.example.cshare.ViewModels.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private LoginViewModel loginViewModel;

    private EditText emailAddressEditText;
    private EditText passwordEditText;
    private Button buttonLogin;

    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            LoginForm loginUser = new LoginForm(emailAddressEditText.getText().toString().trim().toLowerCase(),
                    passwordEditText.getText().toString().trim());
            if (TextUtils.isEmpty(Objects.requireNonNull(emailAddressEditText.getText()))){
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
                loginViewModel.submitValidForm(loginUser);
                loginViewModel.getResponseMutableLiveData().observe(this, new Observer<LoginResponse>() {
                    @Override
                    public void onChanged(LoginResponse loginResponse) {
                        String status = loginResponse.getRequestStatus();
                        LoginResponse.UserResponse user = loginResponse.getUserResponse();
                        String token = loginResponse.getKey();

                        if (loginResponse.getRequestStatus().equals("success")) {

                            Log.i("intent", "gnnnn " + token);

                            int id = user.getId();

                            LauncherActivity.userCreditsEditor.putString("token", token);
                            LauncherActivity.userCreditsEditor.apply();
                            LauncherActivity.userCreditsEditor.putBoolean("logStatus", true);
                            LauncherActivity.userCreditsEditor.putInt("id", id);
                            LauncherActivity.userCreditsEditor.apply();

                            Intent toMainActivityIntent = new Intent();
                            toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(toMainActivityIntent);

                        }
                        else {success = false;}


                    }
                });

            }
        }
    }


}

