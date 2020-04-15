package com.example.cshare.RequestManager;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.LoginResponse;
import com.example.cshare.Models.User;
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
import retrofit2.Retrofit;

public class AuthRequestManager {

    // Data sources dependencies
    private PreferenceProvider prefs;
    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authApi;

    //private MutableLiveData<LoginResponse> loginResponseMutableLiveData;

    private static AuthRequestManager authRequestManager;

    public AuthRequestManager(PreferenceProvider prefs){
        this.prefs = prefs;
        // Define the URL endpoint for the HTTP request.
        this.retrofit = NetworkClient.getRetrofitClient();
        this.authApi = this.retrofit.create(AuthenticationAPI.class);
    }

    public synchronized static AuthRequestManager getInstance(Application application) throws GeneralSecurityException, IOException {
        /**
         * Method that return the current repository object if it exists
         * else it creates new repository and returns it
         */
        if (authRequestManager == null) {
            if (authRequestManager == null) {
                authRequestManager = new AuthRequestManager(new PreferenceProvider(application));
            }
        }
        return authRequestManager;
    }

    public Boolean isLoggedIn(){
        return prefs.isLoggedIn();
    }

    public MutableLiveData<LoginResponse> logIn(LoginForm loginForm) {

        MutableLiveData<LoginResponse> response = new MutableLiveData<>();

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
                    public void onNext(LoginResponse loginResponse) {
                        loginResponse.setRequestStatus(Constants.SUCCESS);
                        response.setValue(loginResponse);
                        Log.d(Constants.TAG, "Log In successful");
                    }

                    @Override
                    public void onError(Throwable e) {
                        response.setValue(new LoginResponse(Constants.ERROR));
                        Log.d(Constants.TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Log In : All data received");
                    }
                });
        return response;
    }

    public void saveUserCredentials(User user){

        prefs.fillPrefs(user);

    }


}
