package com.example.cshare.RequestManager;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.NetworkClient;
import com.example.cshare.WebServices.UserAPI;

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
    private MutableLiveData<User> userProfile = new MutableLiveData<>();

    private Retrofit retrofit;

    // Insert API interface dependency here
    private UserAPI userAPI;

    public ProfileRequestManager() {
        /**
         * Constructor that fetch the user profile and store it in the
         * attributes
         */

        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        userAPI = retrofit.create(UserAPI.class);

        getUserProfile(Constants.TOKEN, Constants.USERID);
    }

    // Getter method
    public MutableLiveData<User> getUser() {
        return userProfile;
    }

    private void getUserProfile(String token, int userID) {
        /**
         * Request to the API to fill the MutableLiveData attribute userProfile with user's info in database
         */

        Observable<User> userObservable;
        userObservable = userAPI.getUserByID(token, userID);
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
                        Log.d(Constants.TAG,"live data filled");
                        userProfile.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                        //productList.setValue((List<Product>) ResponseProductList.error(new NetworkError(e)));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Data received");
                    }
                });

    }

    public synchronized static ProfileRequestManager getInstance() {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (profileRequestManager == null) {
            if (profileRequestManager == null) {
                profileRequestManager = new ProfileRequestManager();
            }
        }
        return profileRequestManager;
    }
}
