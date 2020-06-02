package com.example.cshare.data.sources;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.ApiError;
import com.example.cshare.data.apiresponses.LoginResponse;
import com.example.cshare.data.apiresponses.EmptyAuthResponse;
import com.example.cshare.data.apiresponses.RegistrationResponse;
import com.example.cshare.data.apiresponses.Status;
import com.example.cshare.data.models.Product;
import com.example.cshare.data.models.User;
import com.example.cshare.utils.Constants;
import com.example.cshare.webservices.AuthenticationAPI;
import com.example.cshare.webservices.NetworkClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that holds several MutableLiveData that contain data related to the authentication.
 * <p>
 * The class consists of the 7 following live data:
 *  - isLoggedInMutableLiveData that contains a boolean
 *  - loginResponseMutableLiveData that contains the a LoginResponse instance
 *  - logoutResponseMutableLiveData that contains an EmptyAuthResponse instance
 *  - deleteResponseMutableLiveData that contains an EmptyAuthResponse instance
 *  - changePasswordMutableLiveData that contains an EmptyAuthResponse instance
 *  - resetPasswordMutableLiveData that contains an EmptyAuthResponse instance
 *  - registrationResponseMutableLiveData that contains an RegistrationResponse instance
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that make requests
 * to fill the livedata
 *
 * @see LoginResponse
 * @see EmptyAuthResponse
 * @see RegistrationResponse
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */

public class AuthRequestManager {

    private static AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<LoginResponse> loginResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<EmptyAuthResponse> logoutResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<EmptyAuthResponse> deleteResponseMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<EmptyAuthResponse> changePasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<EmptyAuthResponse> resetPasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<RegistrationResponse> registrationResponseMutableLiveData = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;
    private AuthenticationAPI authenticationAPI;
    private Context context;
    private NetworkClient networkClient = NetworkClient.getInstance();

    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public AuthRequestManager(Context context, PreferenceProvider prefs) {
        this.context = context;
        this.prefs = prefs;
        this.authenticationAPI = this.networkClient.getAuthAPI();
        // Initialize the value of the boolean isLoggedIn
        isLoggedIn();
    }

    /**
     * Method that returns the current repository object if it exists
     * else it creates new repository and returns it
     * @param application
     * @return AuthRequestManager
     */
    public synchronized static AuthRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        if (authRequestManager == null) {
            // create new RequestManager if one does not exist
            authRequestManager = new AuthRequestManager(application, new PreferenceProvider(application));
        }
        // return existing RequestManager
        return authRequestManager;
    }

    // Getter methods
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() {
        return isLoggedInMutableLiveData;
    }

    public MutableLiveData<LoginResponse> getLoginResponseMutableLiveData() {
        return loginResponseMutableLiveData;
    }

    public MutableLiveData<EmptyAuthResponse> getLogoutResponseMutableLiveData() {
        return logoutResponseMutableLiveData;
    }

    public MutableLiveData<EmptyAuthResponse> getDeleteResponseMutableLiveData() {
        return deleteResponseMutableLiveData;
    }

    public MutableLiveData<EmptyAuthResponse> getChangePasswordMutableLiveData() {
        return changePasswordMutableLiveData;
    }

    public MutableLiveData<EmptyAuthResponse> getResetPasswordMutableLiveData() {
        return resetPasswordMutableLiveData;
    }

    public MutableLiveData<RegistrationResponse> getRegistrationResponseMutableLiveData() {
        return registrationResponseMutableLiveData;
    }

    // Requests

    public void isLoggedIn() {
        isLoggedInMutableLiveData.setValue(prefs.isLoggedIn());
    }

    /**
     * This method sends a login form to the API and sets the data that it receives back in
     * loginResponseMutableLiveData
     * It logs the user in
     **/
    public void logIn(User loginForm){
        NetworkClient.getInstance()
                .getAuthAPI()
                .login(loginForm)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            loginResponseMutableLiveData.setValue(LoginResponse.success(response.body().getToken(), response.body().getUser()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.LoginError mError = new ApiError.LoginError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.LoginError.class);
                                loginResponseMutableLiveData.setValue(LoginResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    /**
     * This method fills the response of the login request in the shared preferences
     * It saves user's credentials in shared preferences
     **/
    public void saveUserCredentials(LoginResponse loginResponse) {
        prefs.fillPrefs(loginResponse);
        // Update isLoggedInMutableLiveData
        isLoggedIn();
    }

    public void updateUserCredentials() {
        prefs.logOut();
        // Update isLoggedInMutableLiveData
        isLoggedIn();
    }
    /**
     * This method sends a request to the API to logout and set the response it gets in
     * logoutResponseMutableLiveData
     * It logs the user out
     **/
    public void logOut() {NetworkClient.getInstance()
            .getAuthAPI()
            .logout(prefs.getToken())
            .enqueue(new Callback<EmptyAuthResponse>() {
                @Override
                public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                    if (response.isSuccessful()) {
                        logoutResponseMutableLiveData.setValue(EmptyAuthResponse.success());
                    } else {
                        Gson gson = new GsonBuilder().create();
                        ApiError.ChangePasswordError mError = new ApiError.ChangePasswordError();
                        try {
                            mError= gson.fromJson(response.errorBody().string(), ApiError.ChangePasswordError.class);
                            logoutResponseMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                }
                @Override
                public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                    Log.d(Constants.TAG, t.getLocalizedMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    /**
     * This method sends a register form that has no picture field to the API and sets the data that it receives back in
     * registrationResponseMutableLiveData
     * It registers a new user
     **/
    public void registerWithoutPicture(User registerForm){
        authenticationAPI
                .createUserWithoutPicture(registerForm)
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        if (response.isSuccessful()) {
                            registrationResponseMutableLiveData.setValue(RegistrationResponse.success(response.body().getToken(), response.body().getUser()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.RegistrationError mError = new ApiError.RegistrationError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.RegistrationError.class);
                                registrationResponseMutableLiveData.setValue(RegistrationResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * This method sends a register form that has a picture field to the API and sets the data that it receives back in
     * registrationResponseMutableLiveData
     * It register a new user with picture profile
     **/
    public void registerWithPicture(User registerForm){
        authenticationAPI
                .createUserWithPicture(registerForm.getProfilePictureBody(),
                registerForm.getFirstName(),
                registerForm.getLastName(),
                registerForm.getRoomNumber(),
                registerForm.getCampus(),
                registerForm.getEmail(),
                registerForm.getPassword1(),
                registerForm.getPassword2())
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        if (response.isSuccessful()) {
                            registrationResponseMutableLiveData.setValue(RegistrationResponse.success(response.body().getToken(), response.body().getUser()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.RegistrationError mError = new ApiError.RegistrationError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.RegistrationError.class);
                                registrationResponseMutableLiveData.setValue(RegistrationResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * This method sends a form to the API to change the password and sets the data that it receives back in
     * changePasswordMutableLiveData
     * It changes the password of the user
     **/
    public void changePassword(User changePasswordForm) {
        authenticationAPI
                .changePassword(prefs.getToken(), changePasswordForm)
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            changePasswordMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.ChangePasswordError mError = new ApiError.ChangePasswordError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.ChangePasswordError.class);
                                changePasswordMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This method sends a form to the API to reset password and sets the data that it receives back in
     * changePasswordMutableLiveData
     * It resets the password of the user
     **/
    public void resetPassword(User resetPasswordForm) {

        authenticationAPI
                .resetPassword(resetPasswordForm)
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            changePasswordMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.ChangePasswordError mError = new ApiError.ChangePasswordError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.ChangePasswordError.class);
                                changePasswordMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * This method sends a request to the API to delete an account and sets the data that it receives back in
     * deleteResponseMutableLiveData
     * It deletes the account of the user
     **/
    public void deleteAccount() {
        authenticationAPI
                .delete(prefs.getToken(), prefs.getUserID())
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            deleteResponseMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError.ChangePasswordError mError = new ApiError.ChangePasswordError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), ApiError.ChangePasswordError.class);
                                deleteResponseMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
