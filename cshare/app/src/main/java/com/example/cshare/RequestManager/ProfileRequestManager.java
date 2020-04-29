package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Forms.EditProfileForm;
import com.example.cshare.Models.ApiResponses.ApiEmptyResponse;
import com.example.cshare.Models.ApiResponses.UserReponse;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.UserAPI;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
    // Insert API interface dependency here
    private UserAPI userAPI;
    private AuthenticationAPI authAPI;

    public ProfileRequestManager(PreferenceProvider prefs) {

        this.prefs = prefs;
        // Define the URL endpoint for the HTTP request.
        userAPI = NetworkClient.getInstance().getUserAPI();
        authAPI = NetworkClient.getInstance().getAuthAPI();

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

    public void getUserProfile() {
        /**
         * Request to the API to fill the MutableLiveData attribute userProfile with user's info in database
         */
        Observable<User> userObservable;
        userObservable = authAPI.getProfileInfo(prefs.getToken());
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "getUser : on start subscription");
                        userProfileResponse.setValue(UserReponse.loading());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Constants.TAG, "getUser : live data filled");
                        userProfileResponse.setValue(UserReponse.success(user));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "getUser : error");
                        userProfileResponse.postValue(UserReponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getUser : Data received");
                    }
                });
    }

    public void getUserByID(int userID) {
        /**
         * Request to the API to fill the MutableLiveData attribute userProfile with user's info in database
         */
        Observable<User> userObservable;
        userObservable = userAPI.getUserByID(prefs.getToken(), userID);
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "getUser : on start subscription");
                        otherUserProfileResponse.setValue(UserReponse.loading());
                    }

                    @Override
                    public void onNext(User response) {
                        Log.d(Constants.TAG, "getUser : live data filled");
                        otherUserProfileResponse.setValue(UserReponse.success(response));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "getUser : error");
                        otherUserProfileResponse.setValue(UserReponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "getUser : Data received");
                        otherUserProfileResponse.setValue(UserReponse.complete());
                    }
                });
    }

    public void editProfileWithPicture(EditProfileForm form) {
        /**
         * Request to the API to edit the user's profile with a profile  with a put request
         * @param
         */
        Observable<User> userObservable;
        userObservable = userAPI.updateProfileWithPicture(
                prefs.getToken(),
                prefs.getUserID(),
                form.getProfile_picture(),
                form.getFirst_name(),
                form.getLast_name(),
                form.getRoom_number(),
                form.getCampus());
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Edit profile : on start subscription");
                        userProfileResponse.setValue(UserReponse.loading());
                        editedProfileResponse.setValue(ApiEmptyResponse.loading());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Constants.TAG, "profile edited successfully");
                        editPrefs(user.getCampus());
                        userProfileResponse.setValue(UserReponse.success(user));
                        editedProfileResponse.setValue(ApiEmptyResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "Edit profile : error");
                        userProfileResponse.setValue(UserReponse.error(e));
                        editedProfileResponse.setValue(ApiEmptyResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Edit profile : completed");
                        editedProfileResponse.setValue(ApiEmptyResponse.complete());
                    }
                });
    }

    public void editPrefs(String campus){
        if(!prefs.getCampus().equals(campus)){
            prefs.updateCampus(campus);
        }
    }

    public void editProfileWithoutPicture(EditProfileForm editProfileForm) {
        /**
         * Request to the API to edit the user's profile without a profile  with a put request
         * @param
         */
        Observable<User> userObservable;
        userObservable = userAPI.updateProfileWithoutPicture(
                prefs.getToken(),
                prefs.getUserID(),
                editProfileForm);
        userObservable
                // Run the Observable in a dedicated thread (Schedulers.io)
                .subscribeOn(Schedulers.io())
                // Allows to tell all Subscribers to listen to the Observable data stream on the
                // main thread (AndroidSchedulers.mainThread) which will allow us to modify elements
                // of the graphical interface from the  method
                .observeOn(AndroidSchedulers.mainThread())
                // If the Subscriber has not sent data before the defined time (10 seconds),
                // the data transmission will be stopped and a Timeout error will be sent to the
                // Subscribers via their onError() method.
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Edit profile : on start subscription");
                        userProfileResponse.setValue(UserReponse.loading());
                        editedProfileResponse.setValue(ApiEmptyResponse.loading());
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Constants.TAG, "profile edited successfully");
                        editPrefs(user.getCampus());
                        userProfileResponse.setValue(UserReponse.success(user));
                        editedProfileResponse.setValue(ApiEmptyResponse.success());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        userProfileResponse.setValue(UserReponse.error(e));
                        editedProfileResponse.setValue(ApiEmptyResponse.error(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Edit profile : completed");
                        editedProfileResponse.setValue(ApiEmptyResponse.complete());
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
