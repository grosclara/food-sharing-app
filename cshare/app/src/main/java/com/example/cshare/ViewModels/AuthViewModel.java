package com.example.cshare.ViewModels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import com.example.cshare.Models.Response.ApiEmptyResponse;
import com.example.cshare.Models.Response.LoginResponse;
import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.Auth.RegisterForm;
import com.example.cshare.Models.Auth.ResetPasswordForm;
import com.example.cshare.Models.Response.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.RequestManager.AuthRequestManager;
import com.example.cshare.Models.Auth.LoginForm;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthViewModel extends AndroidViewModel {

    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<ApiEmptyResponse> logoutResponseMutableLiveData;
    private MutableLiveData<UserReponse> deleteResponseMutableLiveData;
    private MutableLiveData<ApiEmptyResponse> changePasswordMutableLiveData;
    private MutableLiveData<ApiEmptyResponse> resetPasswordMutableLiveData;
    private MutableLiveData<LoginResponse> registrationResponseMutableLiveData;

    public AuthViewModel(Application application) throws GeneralSecurityException, IOException {
        super(application);

        // Get request manager instance
        authRequestManager = AuthRequestManager.getInstance(getApplication());

        isLoggedInMutableLiveData = authRequestManager.getIsLoggedInMutableLiveData();
        loginResponseMutableLiveData = authRequestManager.getLoginResponseMutableLiveData();
        logoutResponseMutableLiveData = authRequestManager.getLogoutResponseMutableLiveData();
        deleteResponseMutableLiveData = authRequestManager.getDeleteResponseMutableLiveData();
        changePasswordMutableLiveData = authRequestManager.getChangePasswordMutableLiveData();
        resetPasswordMutableLiveData = authRequestManager.getResetPasswordMutableLiveData();
        registrationResponseMutableLiveData = authRequestManager.getRegistrationResponseMutableLiveData();
    }

    // Getter method

    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() { return isLoggedInMutableLiveData; }
    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData(){ return loginResponseMutableLiveData; }
    public MutableLiveData<ApiEmptyResponse> getLogoutResponseMutableLiveData() { return logoutResponseMutableLiveData; }
    public MutableLiveData<UserReponse> getDeleteResponseMutableLiveData() { return deleteResponseMutableLiveData; }
    public MutableLiveData<ApiEmptyResponse> getChangePasswordMutableLiveData() { return changePasswordMutableLiveData; }
    public MutableLiveData<ApiEmptyResponse> getResetPasswordMutableLiveData() { return resetPasswordMutableLiveData; }
    public MutableLiveData<LoginResponse> getRegistrationResponseMutableLiveData() {return registrationResponseMutableLiveData; }


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