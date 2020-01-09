package com.example.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.frontend.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateAccount;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Buttons
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonSignIn = findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonCreateAccount) {

            Intent toSignUpActivityIntent = new Intent();
            toSignUpActivityIntent.setClass(getApplicationContext(), SignUpActivity.class);
            startActivity(toSignUpActivityIntent);

        } else if (v == buttonSignIn) {
            //Validate the signing up
            Intent toMainActivityIntent = new Intent();
            toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(toMainActivityIntent);
        }
    }
}
