package com.example.cshare.data.sources;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.UserReponse;
import com.example.cshare.data.models.User;
import com.example.cshare.utils.Constants;
import com.example.cshare.webservices.AuthenticationAPI;
import com.example.cshare.webservices.NetworkClient;
import com.example.cshare.webservices.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ProfileRequestManager is a class that sends api calls and holds the user data as
 * attributes
 *
 * @author Clara Gros, Babacar Toure
 * @version 1.0
 */

public class ProfileRequestManager {

    private static ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the user data
    private MutableLiveData<UserReponse> userProfileResponse = new MutableLiveData<>();
    private MutableLiveData<UserReponse> editedProfileResponse = new MutableLiveData<>();
    private MutableLiveData<UserReponse> otherUserProfileResponse = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;
    private NetworkClient networkClient = NetworkClient.getInstance();
    private AuthenticationAPI authenticationAPI;
    private UserAPI userAPI;
    private Context context;

    public ProfileRequestManager(Context context, PreferenceProvider prefs) {
        this.context = context;
        this.authenticationAPI = networkClient.getAuthAPI();
        this.userAPI = networkClient.getUserAPI();
        this.prefs = prefs;
        update();
    }

    public void update() {
        getUserProfile();
    }

    // Getter method
    public MutableLiveData<UserReponse> getUserProfileResponse() {
        return userProfileResponse;
    }
    public MutableLiveData<UserReponse> getOtherUserProfileResponse() { return otherUserProfileResponse; }
    public MutableLiveData<UserReponse> getEditedProfileResponse() { return editedProfileResponse; }

    public void getUserProfile() {
        Log.d(Constants.TAG, "get userprofile");
        authenticationAPI
                .getProfileInfo(prefs.getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUserProfileResponse().setValue(UserReponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            UserReponse.UserError mError = new UserReponse.UserError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), UserReponse.UserError.class);
                                getUserProfileResponse().setValue(UserReponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getUserByID(int userID) {
        userAPI
                .getUserByID(prefs.getToken(), userID)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getOtherUserProfileResponse().setValue(UserReponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            UserReponse.UserError mError = new UserReponse.UserError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), UserReponse.UserError.class);
                                getUserProfileResponse().setValue(UserReponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editProfileWithPicture(User editProfileForm) {
        userAPI
                .updateProfileWithPicture(prefs.getToken(),
                        prefs.getUserID(),
                        editProfileForm.getProfilePictureBody(),
                        editProfileForm.getRoomNumber(),
                        editProfileForm.getCampus())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUserProfileResponse().setValue(UserReponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            UserReponse.UserError mError = new UserReponse.UserError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), UserReponse.UserError.class);
                                getUserProfileResponse().setValue(UserReponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editProfileWithoutPicture(User editProfileForm) {
        userAPI
                .updateProfileWithoutPicture(prefs.getToken(), prefs.getUserID(), editProfileForm)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUserProfileResponse().setValue(UserReponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            UserReponse.UserError mError = new UserReponse.UserError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), UserReponse.UserError.class);
                                getUserProfileResponse().setValue(UserReponse.error(mError));
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(Constants.TAG, t.getLocalizedMessage());
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public synchronized static ProfileRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (profileRequestManager == null) {
            profileRequestManager = new ProfileRequestManager(application, new PreferenceProvider(application));
        }
        return profileRequestManager;
    }
}
