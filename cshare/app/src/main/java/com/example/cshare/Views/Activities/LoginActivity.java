package com.example.cshare.Views.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cshare.Models.Forms.LoginForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.Models.Forms.ResetPasswordForm;
import com.example.cshare.R;
import com.example.cshare.RequestManager.Status;
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
    private Button buttonResetPassword;
    private EditText editTextResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind views
        emailAddressEditText = findViewById(R.id.emailAddressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        //  Set click listeners
        buttonLogin.setOnClickListener(this);
        buttonCreateAccount.setOnClickListener(this);
        buttonResetPassword.setOnClickListener(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        authViewModel.getLoginResponseMutableLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                if (loginResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();

                    // In case of success
                    profileViewModel.update();
                    productViewModel.update();

                    // Redirect to the MainActivity
                    Intent toMainActivityIntent = new Intent();
                    toMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(toMainActivityIntent);

                } else if (loginResponse.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getApplicationContext(), loginResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authViewModel.getLoginResponseMutableLiveData().setValue(LoginResponse.complete());
                }
            }
        });

        authViewModel.getResetPasswordMutableLiveData().observe(this, new Observer<ApiEmptyResponse>() {
            @Override
            public void onChanged(ApiEmptyResponse apiEmptyResponse) {
                if (apiEmptyResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (apiEmptyResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Password reset", Toast.LENGTH_SHORT).show();
                } else if (apiEmptyResponse.getStatus().equals(Status.ERROR)) {
                    Toast.makeText(getApplicationContext(), apiEmptyResponse.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    authViewModel.getResetPasswordMutableLiveData().setValue(ApiEmptyResponse.complete());
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

            case (R.id.buttonResetPassword):
                // Alert Dialog to change password
                // Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Inflate the layout
                View resetPasswordLayout = LayoutInflater.from(this).inflate(R.layout.reset_password, null);

                // Load the edit texts
                editTextResetPassword = resetPasswordLayout.findViewById(R.id.emailAddressEditText);

                builder.setView(resetPasswordLayout);

                // Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Enter your email to reset your password")
                        .setTitle("Reset password")
                        // Add the buttons
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button -> reset password
                                ResetPasswordForm passwordForm = new ResetPasswordForm(editTextResetPassword.getText().toString().trim());

                                // Validation
                                // TODO : set Validators
                                if (passwordForm.isValid()) {
                                    authViewModel.resetPassword(passwordForm);
                                } else {
                                    // Set errors
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
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
