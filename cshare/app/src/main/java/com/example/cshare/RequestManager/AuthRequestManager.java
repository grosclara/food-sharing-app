package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.LoginResponse;
import com.example.cshare.Utils.Constants;
import com.example.cshare.Utils.PreferenceProvider;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthRequestManager {

    private static AuthRequestManager authRequestManager;

    // MutableLiveData object that contains the data
    private MutableLiveData<Boolean> isLoggedInMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ResponseLogin> loginResponseMutableLiveData = new MutableLiveData<>();

    // Data sources dependencies
    private PreferenceProvider prefs;
    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authApi;

    public AuthRequestManager(PreferenceProvider prefs) {

        this.prefs = prefs;
        // Define the URL endpoint for the HTTP request.
        this.retrofit = NetworkClient.getRetrofitClient();
        this.authApi = this.retrofit.create(AuthenticationAPI.class);

        // Initialize the value of the boolean isLoggedIn
        isLoggedIn();
    }

    public synchronized static AuthRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (authRequestManager == null) {
            Log.d("tag", "new instance +1");
            authRequestManager = new AuthRequestManager(new PreferenceProvider(application));
        }
        return authRequestManager;
    }

    // Getter method
    public MutableLiveData<Boolean> getIsLoggedInMutableLiveData() {
        return isLoggedInMutableLiveData;
    }

    // Requests

    public void isLoggedIn() {
        isLoggedInMutableLiveData.setValue(prefs.isLoggedIn());
    }

    public void logIn(LoginForm loginForm) {

        Observable<LoginResponse> loginResponseObservable;
        loginResponseObservable = authApi.login(loginForm);
        loginResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "Log In : On start subscription");
                    }
                    @Override
                    public void onNext(LoginResponse response) {
                        Log.d(Constants.TAG, "Log In successful");
                        saveUserCredentials(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Constants.TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log In : All data received");
                    }
                });
    }

    public void saveUserCredentials(LoginResponse loginResponse){
        prefs.fillPrefs(loginResponse);
        // Update isLoggedInMutableLiveData
        isLoggedIn();
    }


}
