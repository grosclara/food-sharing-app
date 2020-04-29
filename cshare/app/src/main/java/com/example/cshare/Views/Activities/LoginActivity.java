package com.example.cshare.Views.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cshare.Models.Forms.LoginForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.R;
import com.example.cshare.RequestManager.Status;
import com.example.cshare.Utils.Constants;
import com.example.cshare.ViewModels.AuthViewModel;
import com.example.cshare.ViewModels.ProductViewModel;
import com.example.cshare.ViewModels.ProfileViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Form validation
    private Validator loginValidator;

    private AuthViewModel authViewModel;
    private ProductViewModel productViewModel;
    private ProfileViewModel profileViewModel;

    // Views
    @Email
    private EditText emailAddressEditText;
    @Password
    private EditText passwordEditText;
    private EditText resetPasswordEditText;

    private Button buttonLogin;
    private Button buttonCreateAccount;
    private Button buttonResetPassword;

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

        configureValidator();
        configureViewModel();

    }

    private void configureViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getLoginResponseMutableLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse loginResponse) {
                Log.d(Constants.TAG, "LOGIN "+loginResponse.getStatus());
                if (loginResponse.getStatus().equals(Status.LOADING)) {
                    Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();

                    authViewModel.saveUserCredentials(loginResponse);

                    profileViewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ProfileViewModel.class);
                    productViewModel = new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ProductViewModel.class);

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

    private void configureValidator() {

        loginValidator = new Validator(this);

        // Instantiate a new Validator
        loginValidator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                // Retrieve information from the UI
                LoginForm loginUser = new LoginForm(emailAddressEditText.getText().toString().trim().toLowerCase(),
                        passwordEditText.getText().toString().trim());

                authViewModel.logIn(loginUser);
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getApplicationContext());

                    // Display error messages
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            loginValidator.validate();
        }
        else if (v == buttonCreateAccount) {

            // Redirect to the Register Activity
            Intent toRegisterActivityIntent = new Intent();
            toRegisterActivityIntent.setClass(getApplicationContext(), RegisterActivity.class);
            startActivity(toRegisterActivityIntent);

        }
        else if (v == buttonResetPassword) {
            // Alert Dialog to change password
            // Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Inflate the layout
            View resetPasswordLayout = LayoutInflater.from(this).inflate(R.layout.reset_password, null);

            // Load the edit texts
            resetPasswordEditText = resetPasswordLayout.findViewById(R.id.emailAddressEditText);

            builder.setView(resetPasswordLayout);

            // Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Enter your email to reset your password")
                    .setTitle("Reset password")
                    // Add the buttons
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button -> reset password
                            String email = resetPasswordEditText.getText().toString().trim().toLowerCase();
                            if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                authViewModel.resetPassword(email);
                            }else {
                                String message = "Invalid email";
                                // Display error messages
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {/* User cancelled the dialog*/}
            });

            // Create the AlertDialog object and return it
            builder.create().show();
        }

    }
}

