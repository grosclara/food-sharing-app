package com.example.cshare.ViewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import com.example.cshare.Models.Auth.RegisterForm;
import com.example.cshare.RequestManager.AuthRequestManager;
import com.example.cshare.Models.Auth.LoginForm;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthViewModel extends AndroidViewModel {

    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;
    private MutableLiveData<Boolean> isRegisteredMutableLiveData;

    public AuthViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        // Get request manager instance
        authRequestManager = AuthRequestManager.getInstance(getApplication());

        // Initialize isLoggedIn var
        isLoggedInMutableLiveData = authRequestManager.getIsLoggedInMutableLiveData();
        //registerFormMutableLiveData = new MutableLiveData<>();
        isRegisteredMutableLiveData = authRequestManager.getIsRegisteredMutableLiveData();
    }

    // Getter method
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() {
        return isLoggedInMutableLiveData;
    }
    // public MutableLiveData<RegisterForm> getRegisterFormMutableLiveData(){ return registerFormMutableLiveData; }
    public MutableLiveData<Boolean> getIsRegisteredMutableLiveData(){return isRegisteredMutableLiveData;}

    public void logIn(LoginForm loginForm){
        authRequestManager.logIn(loginForm);
    }

    public void logOut() {
        authRequestManager.logOut();
    }

    public void register(RegisterForm registerForm){
        if (registerForm.getProfile_picture() != null){
            authRequestManager.registerWithPicture(registerForm);
        } else { authRequestManager.registerWithoutPicture(registerForm); }
    }
}