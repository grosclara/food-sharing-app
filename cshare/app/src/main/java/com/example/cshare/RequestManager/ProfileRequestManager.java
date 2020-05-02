package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.ApiResponses.EmptyAuthResponse;
import com.example.cshare.Models.Forms.EditProfileForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private MutableLiveData<ApiEmptyResponse> editedProfileResponse = new MutableLiveData<>();
    private MutableLiveData<UserReponse> otherUserProfileResponse = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;

    public ProfileRequestManager(PreferenceProvider prefs) {

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
    public MutableLiveData<ApiEmptyResponse> getEditedProfileResponse() { return editedProfileResponse; }

    public void editPrefs(String campus){
        if(!prefs.getCampus().equals(campus)){
            prefs.updateCampus(campus);
        }
    }

    public void getUserProfile() {
        NetworkClient.getInstance()
                .getAuthAPI()
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
                    }
                });
    }

    public void getUserByID(int userID) {
        NetworkClient.getInstance()
                .getUserAPI()
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
                    }
                });
    }

    public void editProfileWithPicture(EditProfileForm form) {
        NetworkClient.getInstance()
                .getUserAPI()
                .updateProfileWithPicture(prefs.getToken(),
                        prefs.getUserID(),
                        form.getProfile_picture(),
                        form.getFirst_name(),
                        form.getLast_name(),
                        form.getRoom_number(),
                        form.getCampus())
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
                    }
                });
    }

    public void editProfileWithoutPicture(EditProfileForm editProfileForm) {
        NetworkClient.getInstance()
                .getUserAPI()
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
                    }
                });
    }


    public synchronized static ProfileRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (profileRequestManager == null) {
            profileRequestManager = new ProfileRequestManager(new PreferenceProvider(application));
        }
        return profileRequestManager;
    }
}
