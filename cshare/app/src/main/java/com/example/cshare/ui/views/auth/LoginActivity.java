package com.example.cshare.ui.views.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cshare.R;
import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.models.User;
import com.example.cshare.ui.viewmodels.AuthViewModel;
import com.example.cshare.ui.views.HomeScreenActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

/**
 * Fragment which inherits from the BaseFragment class and manages all user information.
 * <p>
 * Implements a profileViewModel to be able to retrieve information from the user but also to edit
 * it. On the other hand, the authViewModel allows to manage several authentication features
 * proposed in this fragment such as password change, account deletion or log out.
 *
 * @see AppCompatActivity
 * @see AuthViewModel
 * @since 1.1
 * @author Clara Gros
 * @author Babacar Toure
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
    Validator.ValidationListener {

    public final static int REGISTER_REQUEST_CODE = 300;

    // Validation
    private Validator loginValidator;

    // Forms
    private User loginForm;
    private User resetPasswordForm;

    private AuthViewModel authViewModel;

    // Views
    @Email
    private EditText emailAddressEditText;
    @Password
    private EditText passwordEditText;
    private EditText resetPasswordEditText;
    // Buttons
    private Button buttonLogin;
    private Button buttonCreateAccount;
    private Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configureDesign();
        configureValidator();
        configureViewModel();

        observeDataChanges();
    }

    private void configureDesign(){
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

        configureActionBar();
    }

    /**
     * Hide ActionBar
     */
    private void configureActionBar() {
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }
    }

    /**
     * Configures ViewModels with default ViewModelProvider
     *
     * @see androidx.lifecycle.ViewModelProvider
     */
    private void configureViewModel() {
        authViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(AuthViewModel.class);
    }

    /**
     * Calls the public methods of our ViewModel to observe their results.
     * <p>
     * For the Get methods, we used the observe() method to be automatically alerted if the
     * database result changes.
     */
    private void observeDataChanges(){
        this.getLoginResponse();
        this.getResetPasswordResponse();
    }

    /**
     * Observe the reset password response from the authViewModel.
     * <p>
     * After a request to reset password, the response status changes to success or failure.
     * In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see AuthViewModel#getResetPasswordMutableLiveData() ()
     * @see EmptyAuthResponse
     */
    private void getResetPasswordResponse(){
        authViewModel.getResetPasswordMutableLiveData().observe(this, new Observer<EmptyAuthResponse>() {
            @Override
            public void onChanged(EmptyAuthResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    Toast.makeText(getApplicationContext(), R.string.password_reset, Toast.LENGTH_SHORT).show();
                    authViewModel.getResetPasswordMutableLiveData().setValue(EmptyAuthResponse.complete());
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    authViewModel.getResetPasswordMutableLiveData().setValue(EmptyAuthResponse.complete());
                }
            }
        });
    }

    /**
     * Observe the log in response from the authViewModel.
     * <p>
     * After a request to log in, the response status changes to success or failure.
     * In case of success, calls the saveUserCredentials method of the authViewModel and redirects
     * to the HomeScreenActivity. In case of failure, toasts an error message.
     * After having done so, Set the status of the response to Complete to indicate the event has
     * been handled.
     *
     * @see AuthViewModel#getLogoutResponseMutableLiveData()
     * @see EmptyAuthResponse
     * @see AuthViewModel#updateUserCredentials()
     */
    private void getLoginResponse(){
        authViewModel.getLoginResponseMutableLiveData().observe(this, new Observer<LoginResponse>() {
            @Override
            public void onChanged(LoginResponse response) {
                if (response.getStatus().equals(Status.SUCCESS)) {
                    authViewModel.saveUserCredentials(response);
                    Toast.makeText(getApplicationContext(), R.string.logged_in_successful, Toast.LENGTH_SHORT).show();
                    authViewModel.getLoginResponseMutableLiveData().setValue(LoginResponse.complete());
                    // Redirect to the MainActivity
                    Intent toMainActivityIntent = new Intent();
                    toMainActivityIntent.setClass(getApplicationContext(), HomeScreenActivity.class);
                    startActivity(toMainActivityIntent);
                } else if (response.getStatus().equals(Status.ERROR)) {
                    if (response.getError().getFieldErrors() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getFieldErrors(), Toast.LENGTH_SHORT).show();
                    }
                    else if (response.getError().getDetail() != null) {
                        Toast.makeText(getApplicationContext(), response.getError().getDetail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_SHORT).show();
                    }
                    authViewModel.getLoginResponseMutableLiveData().setValue(LoginResponse.complete());
                }
            }
        });
    }

    /**
     * Instantiate a new Validator
     *
     * @see Validator
     */
    private void configureValidator() {
        // Instantiate a new Validator
        loginValidator = new Validator(this);
        loginValidator.setValidationListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) { loginValidator.validate();
        } else if (v == buttonCreateAccount) {
            // Redirect to the Register Activity
            Intent toRegisterActivityIntent = new Intent();
            toRegisterActivityIntent.setClass(getApplicationContext(), RegisterActivity.class);
            startActivityForResult(toRegisterActivityIntent, REGISTER_REQUEST_CODE);
        } else if (v == buttonResetPassword) { resetPassword(); }
    }

    /**
     * Callback method called when a user has registered successfully
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REGISTER_REQUEST_CODE)
        { emailAddressEditText.setText(data.getStringExtra("email")); }
    }

    /**
     * Creates and shows an alertDialog to reset password.
     * <p>
     * In case of confirmation, creates a resetPassword form if the form is valid and
     * calls the resetPssword method of the authViewModel
     *
     * @see AlertDialog
     * @see User
     * @see AuthViewModel#resetPassword(User)
     */
    private void resetPassword(){
        // Alert Dialog to change password
        // Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate the layout
        View resetPasswordLayout = LayoutInflater.from(this).inflate(R.layout.reset_password, null);
        // Load the edit texts
        resetPasswordEditText = resetPasswordLayout.findViewById(R.id.emailAddressEditText);
        builder.setView(resetPasswordLayout);
        // Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.reset_password_indications)
                .setTitle(R.string.reset_password)
                // Add the buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button -> reset password
                        String email = resetPasswordEditText.getText().toString().trim().toLowerCase();
                        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            resetPasswordForm = new User(email);
                            authViewModel.resetPassword(resetPasswordForm);
                        }else {
                            // Display error messages
                            Toast.makeText(getApplicationContext(), R.string.invalid_email,  Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {/* User cancelled the dialog*/}
        });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    /**
     * Called when the form has passed all the validations, creates a loginForm (User) object and
     * calls the logIn method of the AuthViewModel.
     *
     * @see Validator
     * @see AuthViewModel#logIn(User)
     */
    @Override
    public void onValidationSucceeded() {
        // Retrieve information from the UI
        loginForm = new User(emailAddressEditText.getText().toString().trim().toLowerCase(),
                passwordEditText.getText().toString().trim());
        authViewModel.logIn(loginForm);
    }

    /**
     * Called when the form hasn't passed all the validations and displays the errors.
     *
     * @see Validator
     */
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
}

