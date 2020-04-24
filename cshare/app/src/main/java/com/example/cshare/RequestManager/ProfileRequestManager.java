package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.Auth.PasswordForm;
import com.example.cshare.Models.EditProfileForm;
import com.example.cshare.Models.Response.UserReponse;
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
    private MutableLiveData<UserReponse> otherUserProfileResponse = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;
    private Retrofit retrofit;
    // Insert API interface dependency here
    private UserAPI userAPI;
    private AuthenticationAPI authAPI;

    public ProfileRequestManager(PreferenceProvider prefs) {

        this.prefs = prefs;
        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        userAPI = retrofit.create(UserAPI.class);
        authAPI = retrofit.create(AuthenticationAPI.class);

        // Initialize the value of the User profile
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
                body,
                form.getFirst_name(),
                form.getLast_name(),
                form.getRoom_number(),
                form.getCampus(),
                email,
                true);
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
                        Log.d(Constants.TAG, "on start subscription");
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Constants.TAG, "profile edited successfully");
                        profileEdited.setValue(true);
                        userProfile.setValue(user);

                    }

                    @Override
                    public void onError(Throwable e) {
                        profileEdited.setValue(false);
                        Log.d(Constants.TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "completed");
                    }
                });
    }

    public void editProfileWithoutPicture(int id, String firstName, String lastName, String roomNumber, String campus, String email) {
        /**
         * Request to the API to edit the user's profile without a profile  with a put request
         * @param
         */
        Observable<User> userObservable;
        userObservable = userAPI.updateProfileWithoutPicture(
                token,
                id,
                firstName,
                lastName,
                roomNumber,
                campus,
                email,
                true);
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
                        Log.d(Constants.TAG, "on start subscription");
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(Constants.TAG, "profile edited successfully");
                        userProfile.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(Constants.TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "completed");
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
