package com.example.cshare.ui.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.models.User;
import com.example.cshare.data.sources.AuthRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthViewModel extends AndroidViewModel {

    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> logoutResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> deleteResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> changePasswordMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> resetPasswordMutableLiveData;
    private MutableLiveData<RegistrationResponse> registrationResponseMutableLiveData;

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
    public MutableLiveData<EmptyAuthResponse> getLogoutResponseMutableLiveData() { return logoutResponseMutableLiveData; }
    public MutableLiveData<EmptyAuthResponse> getDeleteResponseMutableLiveData() { return deleteResponseMutableLiveData; }
    public MutableLiveData<EmptyAuthResponse> getChangePasswordMutableLiveData() { return changePasswordMutableLiveData; }
    public MutableLiveData<EmptyAuthResponse> getResetPasswordMutableLiveData() { return resetPasswordMutableLiveData; }
    public MutableLiveData<RegistrationResponse> getRegistrationResponseMutableLiveData() {return registrationResponseMutableLiveData; }

    public void saveUserCredentials(LoginResponse loginResponse){authRequestManager.saveUserCredentials(loginResponse);}
    public void updateUserCredentials(){authRequestManager.updateUserCredentials();}


    public void logIn(User loginForm){
        authRequestManager.logIn(loginForm);
    }

    public void logOut() {
        authRequestManager.logOut();
    }

    public void deleteAccount() {authRequestManager.deleteAccount();}

    public void register(User registerForm){
        if (registerForm.getProfilePictureBody() != null){
            authRequestManager.registerWithPicture(registerForm);
        } else { authRequestManager.registerWithoutPicture(registerForm); }
    }

    public void changePassword(User changePasswordForm){
        authRequestManager.changePassword(changePasswordForm);
    }

    public void resetPassword(User resetPasswordForm){
        authRequestManager.resetPassword(resetPasswordForm);
    }
}