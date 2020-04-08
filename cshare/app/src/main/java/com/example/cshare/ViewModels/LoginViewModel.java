package com.example.cshare.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;

import com.example.cshare.Models.LoginForm;
import com.example.cshare.Models.User;
import com.example.cshare.Utils.Constants;
import com.example.cshare.WebServices.AuthenticationAPI;
import com.example.cshare.WebServices.NetworkClient;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginViewModel extends ViewModel {


    private MutableLiveData<User> userMutableLiveData;

    private MutableLiveData<LoginForm> loginFormMutableLiveData;

    private Retrofit retrofit;
    // Insert API interface dependency here
    private AuthenticationAPI authAPI;

    public LoginViewModel() {
        // Define the URL endpoint for the HTTP request.
        retrofit = NetworkClient.getRetrofitClient();
        authAPI = retrofit.create(AuthenticationAPI.class);
    }

    public MutableLiveData<User> getUser() {
        return userMutableLiveData;
    }

    public void submitValidForm(LoginForm loginForm) {

        loginFormMutableLiveData = new MutableLiveData<>();
        loginFormMutableLiveData.setValue(loginForm);
        // request
        Observable<LoginForm> loginFormObservable;
        loginFormObservable = authAPI.login(
            loginFormMutableLiveData.getValue());
        loginFormObservable
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
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(Constants.TAG, "on start subscription");
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        String token = loginResponse.getToken();
                        User user =  loginResponse.getUser();
                        Log.d(Constants.TAG, "Logged in successfully!");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(Constants.TAG, "error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Constants.TAG, "Product received successfully");
                    }
                });

    }

}