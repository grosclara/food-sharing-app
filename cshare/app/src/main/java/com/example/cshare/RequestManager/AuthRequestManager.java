package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.LoginResponse;
import com.example.cshare.Models.ApiResponses.EmptyAuthResponse;
import com.example.cshare.Models.Forms.LoginForm;
import com.example.cshare.Models.Forms.PasswordForm;
import com.example.cshare.Models.Forms.RegisterForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.RegistrationResponse;
import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public AuthRequestManager(PreferenceProvider prefs) {

        this.prefs = prefs;
        // Initialize the value of the boolean isLoggedIn
        isLoggedIn();
    }

    public synchronized static AuthRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (authRequestManager == null) {
            authRequestManager = new AuthRequestManager(new PreferenceProvider(application));
        }
        return authRequestManager;
    }

    // Getter method
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

    public void logIn(LoginForm loginForm){
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
                            LoginResponse.LoginError mError = new LoginResponse.LoginError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), LoginResponse.LoginError.class);
                                loginResponseMutableLiveData.setValue(LoginResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });

    }

    public void saveUserCredentials(LoginResponse loginResponse) {
        prefs.fillPrefs(loginResponse);
        // Update isLoggedInMutableLiveData
        isLoggedIn();
    }

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
                        EmptyAuthResponse.EmptyAuthError mError = new EmptyAuthResponse.EmptyAuthError();
                        try {
                            mError= gson.fromJson(response.errorBody().string(), EmptyAuthResponse.EmptyAuthError.class);
                            logoutResponseMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                        } catch (IOException e) {
                            // handle failure to read error
                        }
                    }
                }
                @Override
                public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                    Log.d(Constants.TAG, t.getLocalizedMessage());
                }
            });
    }

    public void registerWithoutPicture(RegisterForm user){
        NetworkClient.getInstance()
                .getAuthAPI()
                .createUserWithoutPicture(user)
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        if (response.isSuccessful()) {
                            registrationResponseMutableLiveData.setValue(RegistrationResponse.success(response.body().getToken(), response.body().getUser()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            RegistrationResponse.RegistrationError mError = new RegistrationResponse.RegistrationError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), RegistrationResponse.RegistrationError.class);
                                registrationResponseMutableLiveData.setValue(RegistrationResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });

    }

    public void registerWithPicture(RegisterForm user){
        NetworkClient.getInstance()
                .getAuthAPI()
                .createUserWithPicture(user.getProfile_picture(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getRoom_number(),
                user.getCampus(),
                user.getEmail(),
                user.getPassword1(),
                user.getPassword2())
                .enqueue(new Callback<RegistrationResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                        if (response.isSuccessful()) {
                            registrationResponseMutableLiveData.setValue(RegistrationResponse.success(response.body().getToken(), response.body().getUser()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            RegistrationResponse.RegistrationError mError = new RegistrationResponse.RegistrationError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), RegistrationResponse.RegistrationError.class);
                                registrationResponseMutableLiveData.setValue(RegistrationResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });

    }

    public void changePassword(PasswordForm passwordForm) {
        NetworkClient.getInstance()
                .getAuthAPI()
                .changePassword(prefs.getToken(), passwordForm)
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            changePasswordMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            EmptyAuthResponse.EmptyAuthError mError = new EmptyAuthResponse.EmptyAuthError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), EmptyAuthResponse.EmptyAuthError.class);
                                changePasswordMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });
    }

    public void resetPassword(String email) {

        Map<String, String> emailFieldMap = new HashMap<>();
        emailFieldMap.put("email", email);

        NetworkClient.getInstance()
                .getAuthAPI()
                .resetPassword(emailFieldMap)
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            changePasswordMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            EmptyAuthResponse.EmptyAuthError mError = new EmptyAuthResponse.EmptyAuthError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), EmptyAuthResponse.EmptyAuthError.class);
                                changePasswordMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });
    }

    public void deleteAccount() {
        NetworkClient.getInstance()
                .getAuthAPI()
                .delete(prefs.getToken(), prefs.getUserID())
                .enqueue(new Callback<EmptyAuthResponse>() {
                    @Override
                    public void onResponse(Call<EmptyAuthResponse> call, Response<EmptyAuthResponse> response) {
                        if (response.isSuccessful()) {
                            deleteResponseMutableLiveData.setValue(EmptyAuthResponse.success());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            EmptyAuthResponse.EmptyAuthError mError = new EmptyAuthResponse.EmptyAuthError();
                            try {
                                mError= gson.fromJson(response.errorBody().string(), EmptyAuthResponse.EmptyAuthError.class);
                                deleteResponseMutableLiveData.setValue(EmptyAuthResponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<EmptyAuthResponse> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                    }
                });
    }
}
