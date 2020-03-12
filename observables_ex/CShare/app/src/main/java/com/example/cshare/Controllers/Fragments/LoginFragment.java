package com.example.cshare.Controllers.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cshare.Models.Product;
import com.example.cshare.Models.User;
import com.example.cshare.R;
import com.example.cshare.Utils.ApiStreams;
import com.example.cshare.Utils.Camera;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Future;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class LoginFragment extends BaseFragment implements Validator.ValidationListener {

    // FOR DESIGN
    // Declare form fields
    @BindView(R.id.fragment_login_email)
    @Email
    EditText editTextEmail;
    @BindView(R.id.fragment_login_password)
    @Password
    EditText editTextPassword;
    @BindView(R.id.fragment_login_login)
    Button buttonLogin;
    @BindView(R.id.fragment_login_loading)
    ProgressBar progressBarLoading;

    // FOR DATA

    // Declare Callback
    private LoginFragment.OnButtonClickedListener buttonClickedCallback;

    //HTTP requests
    private Disposable disposable;

    // Declare credentials
    private String email;
    private String password;

    // Form validation
    private Validator validator;
    private Boolean isValid = false;

    // --------------
    // BASE METHODS
    // --------------

    @Override
    protected BaseFragment newInstance() {
        return (new LoginFragment());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_login;
    }

    // We call here the callback creation method from onAttach(), because it is only then that
    // we know for sure that our fragment is well attached to its parent activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Call the method that creating callback after being attached to parent activity
        this.createCallbackToParentActivity();
    }


    @Override
    protected void configureDesign() { configureValidator(); }

    @Override
    protected void updateDesign() { }

    // Declare our interface that will be implemented by any container activity
    // implement the Callback that will allow us to communicate with our parent activity
    public interface OnButtonClickedListener { void onButtonClicked(View view); }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureValidator() {
        // Instantiate a new Validator
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    // ------------------------------
    //  HTTP REQUEST (RxJAVA)
    // ------------------------------

    private void HttpRequestSignIn() {

        User loginForm = new User(email, password);
        // this.updateUIWhenStartingHTTPRequest();
        this.disposable = ApiStreams.streamLogin( loginForm)
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.e("TAG", "On Next");
                        Toast.makeText(getContext(), "Signed in",
                                Toast.LENGTH_SHORT).show();

                        //updateUIWithUserInfo(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "On Error" + Log.getStackTraceString(e));
                        Toast.makeText(getContext(), "Invalid credentials",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "On Complete !!");
                    }
                });
    }

    // --------------
    // UPDATE UI
    // --------------

    // --------------
    // ACTIONS
    // --------------

    // Here we will propagate our user's click directly to our parent activity via the
    // onButtonClicked(View) method.
    @OnClick(R.id.fragment_login_login)
    protected void login(View v) {

        // Validate the form
        validator.validate();

        // Spread the click to the parent activity
            buttonClickedCallback.onButtonClicked(v);
    }

    // --------------
    // FRAGMENT SUPPORT
    // --------------

    // Create callback to parent activity
    // Link our Callback with our parent business by subscribing to it from the child fragment.
    // However, our parent activity (which contains this fragment) will have to implement the
    // OnButtonClickedListener interface.
    protected void createCallbackToParentActivity() {
        try {
            //Parent activity will automatically subscribe to callback
            buttonClickedCallback = (LoginFragment.OnButtonClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnButtonClickedListener");
        }
    }

    // --------------
    // CALLBACKS
    // --------------

    @Override
    public void onValidationSucceeded() {
        //Called when all your views pass all validations

        // Retrieve the name and quantity of the product typed in the editText fields
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());

        // Call the stream
        this.HttpRequestSignIn();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        //Called when there are validation error(s)
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
