package com.example.cshare.data.sources;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.data.apiresponses.ApiError;
import com.example.cshare.data.apiresponses.UserResponse;
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
 * Class that holds several MutableLiveData that contain data related to the profile.
 * <p>
 * The class mainly consists of the 3 following live data:
 *  - userProfileResponse that contains the a UserResponse instance
 *  - editedProfileResponse that contains the a UserResponse instance
 *  - otherUserProfileResponse that contains the a UserResponse instance
 * <p>
 * The defined methods are on the one hand the getters and on the other hand methods that make requests
 * to fill the livedata
 *
 * @see UserResponse
 * @since 2.0
 * @author Clara Gros
 * @author Babacar Toure
 */


public class ProfileRequestManager {

    private static ProfileRequestManager profileRequestManager;

    // MutableLiveData object that contains the user data
    private MutableLiveData<UserResponse> userProfileResponse = new MutableLiveData<>();
    private MutableLiveData<UserResponse> editedProfileResponse = new MutableLiveData<>();
    private MutableLiveData<UserResponse> otherUserProfileResponse = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;
    private NetworkClient networkClient = NetworkClient.getInstance();
    private AuthenticationAPI authenticationAPI;
    private UserAPI userAPI;
    private Context context;
    /**
     * Class constructor
     *
     * @param context
     * @param prefs
     */
    public ProfileRequestManager(Context context, PreferenceProvider prefs) {
        this.context = context;
        this.authenticationAPI = networkClient.getAuthAPI();
        this.userAPI = networkClient.getUserAPI();
        this.prefs = prefs;
        update();
    }

    public void update() { getUserProfile(); }

    // Getter method
    public MutableLiveData<UserResponse> getUserProfileResponse() {
        return userProfileResponse;
    }
    public MutableLiveData<UserResponse> getOtherUserProfileResponse() { return otherUserProfileResponse; }

    /**
     * This method sends a token to the API and sets the data that it receives back in
     * userProfileResponse
     * It retrieves user's info
     **/
    public void getUserProfile() {
        authenticationAPI
                .getProfileInfo(prefs.getToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUserProfileResponse().setValue(UserResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
                                getUserProfileResponse().setValue(UserResponse.error(mError));
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

    /**
     * This method sends a token to the API and sets the data that it receives back in
     * userProfileResponse
     * It retrieves user's info given his id
     **/
    public void getUserByID(int userID) {
        userAPI
                .getUserByID(prefs.getToken(), userID)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getOtherUserProfileResponse().setValue(UserResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
                                getOtherUserProfileResponse().setValue(UserResponse.error(mError));
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

    /**
     * This method sends a token and a user form with a picture field to the API and sets the data that it receives back in
     * userProfileResponse
     * It edits the user's info
     **/
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
                            getUserProfileResponse().setValue(UserResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
                                getUserProfileResponse().setValue(UserResponse.error(mError));
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

    /**
     * This method sends a token and a user form without a picture field to the API and sets the data that it receives back in
     * userProfileResponse
     * It edits the user's info
     **/
    public void editProfileWithoutPicture(User editProfileForm) {
        userAPI
                .updateProfileWithoutPicture(prefs.getToken(), prefs.getUserID(), editProfileForm)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            getUserProfileResponse().setValue(UserResponse.success(response.body()));
                        } else {
                            Gson gson = new GsonBuilder().create();
                            ApiError mError = new ApiError();
                            try {
                                mError = gson.fromJson(response.errorBody().string(), ApiError.class);
                                getUserProfileResponse().setValue(UserResponse.error(mError));
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

    /**
     * Method that returns the current repository object if it exists
     * else it creates new repository and returns it
     * @param application
     * @return ProfileRequestManager
     */
    public synchronized static ProfileRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        if (profileRequestManager == null) {
            profileRequestManager = new ProfileRequestManager(application, new PreferenceProvider(application));
        }
        return profileRequestManager;
    }
}
