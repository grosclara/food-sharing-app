package com.example.cshare.ui.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.models.User;
import com.example.cshare.data.sources.AuthRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * ViewModel class responsible for preparing and managing the authentication-related data for
 * an Activity or a Fragment.
 * <p>
 * This class provides one the one hand getter methods to retrieve data from the attributes.
 * One the other hand,  it provides methods that will directly call request manager methods to
 * perform some actions on the data
 *
 * @see AndroidViewModel
 * @see MutableLiveData
 * @see AuthRequestManager
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */
public class AuthViewModel extends AndroidViewModel {

    /**
     * Repository that will fetch the auth-related data from the remote API source
     */
    private AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData;
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> logoutResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> deleteResponseMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> changePasswordMutableLiveData;
    private MutableLiveData<EmptyAuthResponse> resetPasswordMutableLiveData;
    private MutableLiveData<RegistrationResponse> registrationResponseMutableLiveData;

    /**
     * Constructor of the ViewModel.
     * Takes in an Application parameter that is provided to retrieve the request manager via the
     * getInstance method
     *
     * @param application
     * @throws GeneralSecurityException
     * @throws IOException
     * @see AuthRequestManager#getInstance(Application)
     */
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

    /**
     * Calls the saveUserCredentials method of the request manager
     *
     * @param loginResponse (LoginResponse)
     * @see AuthRequestManager#saveUserCredentials(LoginResponse)
     */
    public void saveUserCredentials(LoginResponse loginResponse){authRequestManager.saveUserCredentials(loginResponse);}
    /**
     * Calls the updateUserCredentials method of the request manager
     *
     * @see AuthRequestManager#updateUserCredentials() (User)
     */
    public void updateUserCredentials(){authRequestManager.updateUserCredentials();}
    /**
     * Calls the logIn method of the request manager
     *
     * @param loginForm (User)
     * @see AuthRequestManager#logIn(User)
     */
    public void logIn(User loginForm){ authRequestManager.logIn(loginForm); }
    /**
     * Calls the logOut method of the request manager
     *
     * @see AuthRequestManager#logOut()
     */
    public void logOut() { authRequestManager.logOut(); }
    /**
     * Calls the delete method of the request manager
     *
     * @see AuthRequestManager#deleteAccount()
     */
    public void deleteAccount() {authRequestManager.deleteAccount();}
    /**
     * Either calls the registerWithPicture or registerWithoutPicture method of the request manager
     * depending on the registerForm.
     *
     * @param registerForm (User)
     * @see AuthRequestManager#registerWithPicture(User)
     * @see AuthRequestManager#registerWithoutPicture(User)
     */
    public void register(User registerForm){
        if (registerForm.getProfilePictureBody() != null){
            authRequestManager.registerWithPicture(registerForm);
        } else { authRequestManager.registerWithoutPicture(registerForm); }
    }
    /**
     * Calls the changePassword method of the request manager
     *
     * @param changePasswordForm (User)
     * @see AuthRequestManager#changePassword(User)
     */
    public void changePassword(User changePasswordForm){
        authRequestManager.changePassword(changePasswordForm);
    }
    /**
     * Calls the resetPassword method of the request manager
     *
     * @param resetPasswordForm (User)
     * @see AuthRequestManager#resetPassword(User)
     */
    public void resetPassword(User resetPasswordForm){
        authRequestManager.resetPassword(resetPasswordForm);
    }
}