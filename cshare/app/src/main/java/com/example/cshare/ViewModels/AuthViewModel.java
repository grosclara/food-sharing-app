package com.example.cshare.ViewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import com.example.cshare.Models.Auth.Response.AuthResponse;
import com.example.cshare.Models.Auth.Response.LoginResponse;
import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.Auth.RegisterForm;
import com.example.cshare.Models.Auth.ResetPasswordForm;
import com.example.cshare.RequestManager.AuthRequestManager;
import com.example.cshare.Models.Auth.LoginForm;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthViewModel extends AndroidViewModel {

    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;
    private MutableLiveData<Boolean> isRegisteredMutableLiveData;
    private MutableLiveData<Boolean> isPasswordChangedMutableLiveData;
    private MutableLiveData<Boolean> isPasswordResetMutableLiveData;

    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<AuthResponse> logoutResponseMutableLiveData;
    private MutableLiveData<AuthResponse> deleteResponseMutableLiveData;

    public AuthViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        // Get request manager instance
        authRequestManager = AuthRequestManager.getInstance(getApplication());

        // Initialize isLoggedIn var
        isLoggedInMutableLiveData = authRequestManager.getIsLoggedInMutableLiveData();
        //registerFormMutableLiveData = new MutableLiveData<>();
        isRegisteredMutableLiveData = authRequestManager.getIsRegisteredMutableLiveData();
        isPasswordChangedMutableLiveData = authRequestManager.getIsPasswordChangedMutableLiveData();
        isPasswordResetMutableLiveData = authRequestManager.getIsPasswordResetMutableLiveData();

        loginResponseMutableLiveData = authRequestManager.getLoginResponseMutableLiveData();
        logoutResponseMutableLiveData = authRequestManager.getLogoutResponseMutableLiveData();
        deleteResponseMutableLiveData = authRequestManager.getDeleteResponseMutableLiveData();
    }

    // Getter method
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() { return isLoggedInMutableLiveData; }
    // public MutableLiveData<RegisterForm> getRegisterFormMutableLiveData(){ return registerFormMutableLiveData; }
    public MutableLiveData<Boolean> getIsRegisteredMutableLiveData(){return isRegisteredMutableLiveData;}
    public MutableLiveData<Boolean> getIsPasswordChangedMutableLiveData(){ return isPasswordChangedMutableLiveData;}
    public MutableLiveData<Boolean> getIsPasswordResetMutableLiveData(){return isPasswordResetMutableLiveData;}

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData(){ return loginResponseMutableLiveData; }
    public MutableLiveData<AuthResponse> getLogoutResponseMutableLiveData() {return logoutResponseMutableLiveData; }
    public MutableLiveData<AuthResponse> getDeleteResponseMutableLiveData() {return deleteResponseMutableLiveData; }

    public void logIn(LoginForm loginForm){
        authRequestManager.logIn(loginForm);
    }

    public void logOut() {
        authRequestManager.logOut();
    }

    public void deleteAccount() {authRequestManager.deleteAccount();}

    public void register(RegisterForm registerForm){
        if (registerForm.getProfile_picture() != null){
            authRequestManager.registerWithPicture(registerForm);
        } else { authRequestManager.registerWithoutPicture(registerForm); }
    }

    public void changePassword(PasswordForm passwordForm){
        authRequestManager.changePassword(passwordForm);
    }

    public void resetPassword(ResetPasswordForm resetPasswordForm){
        authRequestManager.resetPassword(resetPasswordForm);
    }
}